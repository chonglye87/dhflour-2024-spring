package com.dhflour.dhflourdemo1.core.service.jwt;

import com.dhflour.dhflourdemo1.core.types.jwt.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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


/**
 * 대칭키 알고리즘 (Symmetric Key Algorithms)
 * HS256:256비트	높은 보안 강도,빠름,웹 토큰 서명, 일반적인 보안 애플리케이션
 * HS384:384비트	더 높은 보안 강도,중간,금융 서비스, 추가 보안이 필요한 환경
 * HS512:512비트	가장 높은 보안 강도,느림,최고 수준의 보안이 필요한 환경
 * 대칭키 알고리즘은 하나의 비밀 키를 사용하여 토큰을 서명하고 검증합니다. 이 방법은 빠르고 간단하지만, 같은 키를 공유해야 하므로 키 관리에 주의가 필요합니다.
 */
@Slf4j
@Service
public class JWTSymmetricServiceImpl implements JWTSymmetricService {

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.symmetric.secretKey}")
    private String secretKey;

    @Value("${jwt.symmetric.algorithm}")
    private int algorithm;

    // HMAC-SHA-512 알고리즘 유형
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
            KeyGenerator keyGen = KeyGenerator.getInstance(algorithm); // "HmacSHA256"
            keyGen.init(SIGNATURE_ALGORITHM.getKeyBitLength()); // 키 크기 설정 256

            // 비밀 키 생성
            SecretKey secretKey = keyGen.generateKey();

            // 비밀 키를 Base64 형식으로 인코딩
            String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

            // 인코딩된 비밀 키 출력
            log.debug("JWT Symmetric Secret Key: " + encodedKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ES256 (ECDSA with P-256 and SHA-256)
    @Override
    public String generateToken(MyUserDetails user) {
        try {
            // 현재 시간 설정
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);

            // JWT 생성
            return Jwts.builder()
                    .id(String.valueOf(user.getId())) // 토큰 ID 설정
                    .claim("username", user.getUsername()) // 사용자명 클레임 설정
                    .issuer("issuer") // 발행자 설정
                    .subject(user.getEmail()) // 주제 설정
                    .issuedAt(now) // 발행 시간 설정
                    .expiration(new Date(nowMillis + expiration)) // 만료 시간 설정 (1시간 후)
                    .notBefore(now) // 유효 시작 시간 설정
                    .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)), SIGNATURE_ALGORITHM) // 서명 알고리즘과 SecretKey 설정
                    .compact(); // 토큰 생성 및 컴팩트화
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
