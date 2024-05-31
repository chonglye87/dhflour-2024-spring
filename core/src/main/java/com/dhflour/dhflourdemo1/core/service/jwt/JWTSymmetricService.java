package com.dhflour.dhflourdemo1.core.service.jwt;

import com.dhflour.dhflourdemo1.core.types.jwt.MyUserDetails;
import io.jsonwebtoken.Claims;

/**
 * 대칭키 알고리즘의 JWT
 */
public interface JWTSymmetricService {

    void createSecretKey();

    String generateToken(MyUserDetails user);

    Claims verifyToken(String jwtToken);

    String extractSubject(String token);

    Boolean isTokenExpired(String token);

}
