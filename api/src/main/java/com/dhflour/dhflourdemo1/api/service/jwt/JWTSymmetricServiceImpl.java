package com.dhflour.dhflourdemo1.api.service.jwt;

import com.dhflour.dhflourdemo1.core.types.jwt.UserSampleBody;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Date;

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

    private static String SECRET_KEY;
    // HMAC-SHA-512 알고리즘 유형
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    @Override
    public void createSecretKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(SIGNATURE_ALGORITHM.getJcaName());
            keyGen.init(SIGNATURE_ALGORITHM.getMinKeyLength()); // 키 크기 설정

            // 비밀 키 생성
            SecretKey secretKey = keyGen.generateKey();

            // 비밀 키를 Base64 형식으로 인코딩
            String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

            // 인코딩된 비밀 키 출력
            SECRET_KEY = encodedKey;
            log.debug("Base64 Encoded Secret Key: " + SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ES256 (ECDSA with P-256 and SHA-256)
    @Override
    public String generateToken(UserSampleBody user) {
        try {
            // Base64 인코딩된 키를 디코딩하여 SecretKey 생성
            SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));

            // 현재 시간 설정
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);

            // JWT 생성
            return Jwts.builder()
                    .id(String.valueOf(user.getId())) // 토큰 ID 설정
                    .claim("username", user.getUsername()) // 사용자명 클레임 설정
                    .issuer("issuer") // 발행자 설정
                    .subject("subject") // 주제 설정
                    .issuedAt(now) // 발행 시간 설정
                    .expiration(new Date(nowMillis + 3600000)) // 만료 시간 설정 (1시간 후)
                    .notBefore(now) // 유효 시작 시간 설정
                    .signWith(secretKey, SIGNATURE_ALGORITHM) // 서명 알고리즘과 SecretKey 설정
                    .compact(); // 토큰 생성 및 컴팩트화
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate token", e);
        }
    }

    @Override
    public Claims verifyToken(String jwtToken) {
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(secretKey).build()
                .parseSignedClaims(jwtToken);

        return claimsJws.getPayload();
    }
}
