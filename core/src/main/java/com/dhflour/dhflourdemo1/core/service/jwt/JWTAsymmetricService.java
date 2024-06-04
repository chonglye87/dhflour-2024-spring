package com.dhflour.dhflourdemo1.core.service.jwt;

import io.jsonwebtoken.Claims;
import com.dhflour.dhflourdemo1.core.types.jwt.MyUserDetails;

/**
 * 비대칭키 알고리즘의 JWT
 */
public interface JWTAsymmetricService {

    void createSecretKey();

    String generateToken(MyUserDetails user);

    Claims verifyToken(String jwtToken);

    String extractSubject(String token);

    Boolean isTokenExpired(String token);
}
