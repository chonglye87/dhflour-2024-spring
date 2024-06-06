package com.dhflour.dhflourdemo1.core.service.jwt;

import com.dhflour.dhflourdemo1.core.types.jwt.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Service
public class JWTSymmetricServiceImpl implements JWTSymmetricService {

    @Value("${jwt.access.expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    @Value("${jwt.symmetric.secretKey}")
    private String secretKey;

    @Value("${jwt.symmetric.algorithm}")
    private int algorithm;

    private MacAlgorithm SIGNATURE_ALGORITHM = Jwts.SIG.HS256;

    @PostConstruct
    public void init() {
        switch (algorithm) {
            case 256:
                SIGNATURE_ALGORITHM = Jwts.SIG.HS256;
                break;
            case 384:
                SIGNATURE_ALGORITHM = Jwts.SIG.HS384;
                break;
            case 512:
                SIGNATURE_ALGORITHM = Jwts.SIG.HS512;
                break;
            default:
                throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
        }
        log.debug("Algorithm set to: {}", SIGNATURE_ALGORITHM);
    }

    @Override
    public void createSecretKey() {
        try {
            final String algorithm = String.format("HmacSHA%s", SIGNATURE_ALGORITHM.getKeyBitLength());
            KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
            keyGen.init(SIGNATURE_ALGORITHM.getKeyBitLength());

            SecretKey secretKey = keyGen.generateKey();
            String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

            log.debug("JWT Symmetric Secret Key: " + encodedKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String generateToken(MyUserDetails user) {
        return generateToken(user, accessExpiration);
    }

    @Override
    public String generateRefreshToken(MyUserDetails user) {
        return generateToken(user, refreshExpiration);
    }

    private String generateToken(MyUserDetails user, long expiration) {
        try {
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);

            return Jwts.builder()
                    .id(String.valueOf(user.getId()))
                    .claim("username", user.getUsername())
                    .issuer("issuer")
                    .subject(user.getEmail())
                    .issuedAt(now)
                    .expiration(new Date(nowMillis + expiration))
                    .notBefore(now)
                    .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)), SIGNATURE_ALGORITHM)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate token", e);
        }
    }

    @Override
    public Claims verifyToken(String jwtToken) {
        log.debug("JWT Token: {}", jwtToken);
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .build()
                .parseSignedClaims(jwtToken);

        return claimsJws.getPayload();
    }

    @Override
    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = verifyToken(token);
        return claimsResolver.apply(claims);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
