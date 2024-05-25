package com.dhflour.dhflourdemo1.api.service.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.dhflour.dhflourdemo1.core.types.jwt.UserSampleBody;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.InvalidKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

/**
 * 비대칭키 알고리즘 (Asymmetric Key Algorithms)
 * RSA: 널리 사용되는 고강도 암호화 및 서명 알고리즘, 웹 토큰 서명 및 검증에 적합합니다.
 */
@Slf4j
@Service
public class JWTAsymmetricServiceImpl implements JWTAsymmetricService {

    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;

    private static PrivateKey privateKey;
    private static PublicKey publicKey;


    @Override
    public void createSecretKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(signatureAlgorithm.getFamilyName());
            keyPairGenerator.initialize(signatureAlgorithm.getMinKeyLength()); // 2048 비트 키 크기
            KeyPair keyPair = keyPairGenerator.genKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
            log.debug("privateKey : {}", privateKey);
            log.debug("publicKey : {}", publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String generateToken(UserSampleBody user) {
        try {
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);

            // JWT 생성
            return Jwts.builder()
                    .setId(String.valueOf(user.getId())) // 토큰 ID 설정
                    .claim("username", user.getUsername()) // 사용자명 클레임 설정
                    .setIssuer("issuer") // 발행자 설정
                    .setSubject("subject") // 주제 설정
                    .setIssuedAt(now) // 발행 시간 설정
                    .setExpiration(new Date(nowMillis + 3600000)) // 만료 시간 설정 (1시간 후)
                    .setNotBefore(now) // 유효 시작 시간 설정
                    .signWith(privateKey, signatureAlgorithm) // 서명 알고리즘과 개인 키 설정
                    .compact(); // 토큰 생성 및 컴팩트화
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate token", e);
        }
    }

    @Override
    public Claims verifyToken(String jwtToken) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(jwtToken);

            return claimsJws.getBody();
        } catch (InvalidKeyException e) {
            log.error("Invalid public key", e);
            throw new RuntimeException("Failed to verify token", e);
        }
    }
}
