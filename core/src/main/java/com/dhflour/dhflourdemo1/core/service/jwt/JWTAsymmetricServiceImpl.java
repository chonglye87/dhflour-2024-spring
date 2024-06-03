package com.dhflour.dhflourdemo1.core.service.jwt;

import com.dhflour.dhflourdemo1.core.types.jwt.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

/**
 * 비대칭키 알고리즘 (Asymmetric Key Algorithms)
 * RSA: 널리 사용되는 고강도 암호화 및 서명 알고리즘, 웹 토큰 서명 및 검증에 적합합니다.
 */
@Slf4j
@Service
public class JWTAsymmetricServiceImpl implements JWTAsymmetricService {

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.asymmetric.privateKey}")
    private String privateKey;

    @Value("${jwt.asymmetric.publicKey}")
    private String publicKey;

    @Value("${jwt.asymmetric.algorithm}")
    private int algorithm;

    private SignatureAlgorithm SIGNATURE_ALGORITHM = Jwts.SIG.RS256;
    private int keySize  = 2048;

    @PostConstruct
    public void init() {
        switch (algorithm) {
            case 256:
                SIGNATURE_ALGORITHM = Jwts.SIG.RS256;
                keySize = 2048;
                break;
            case 384:
                SIGNATURE_ALGORITHM = Jwts.SIG.RS384;
                keySize = 3072;
                break;
            case 512:
                SIGNATURE_ALGORITHM = Jwts.SIG.RS512;
                keySize = 4096;
                break;
            default:
                throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
        }
        log.debug("Algorithm set to: {}, Key size set to: {}", SIGNATURE_ALGORITHM, keySize);
    }



    @Override
    public void createSecretKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keySize); // 2048 비트 키 크기
            KeyPair keyPair = keyPairGenerator.genKeyPair();

            String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
            String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

            log.debug("privateKey : {}", privateKey);
            log.debug("publicKey : {}", publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String generateToken(MyUserDetails user) {
        try {
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey decodedPrivateKey = keyFactory.generatePrivate(privateKeySpec);

            // JWT 생성
            return Jwts.builder()
                    .id(String.valueOf(user.getId())) // 토큰 ID 설정
                    .claim("username", user.getUsername()) // 사용자명 클레임 설정
                    .issuer("issuer") // 발행자 설정
                    .subject("subject") // 주제 설정
                    .issuedAt(now) // 발행 시간 설정
                    .expiration(new Date(nowMillis + expiration)) // 만료 시간 설정 (1시간 후)
                    .notBefore(now) // 유효 시작 시간 설정
                    .signWith(decodedPrivateKey, SIGNATURE_ALGORITHM) // 서명 알고리즘과 개인 키 설정
                    .compact(); // 토큰 생성 및 컴팩트화
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate token", e);
        }
    }

    @Override
    public Claims verifyToken(String jwtToken) {
        try {

            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey decodedPublicKey = keyFactory.generatePublic(publicKeySpec);

            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(decodedPublicKey)
                    .build()
                    .parseSignedClaims(jwtToken);

            return claimsJws.getPayload();
        } catch (Exception e) {
            log.error("Invalid public key", e);
            throw new RuntimeException("Failed to verify token", e);
        }
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
