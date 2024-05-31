package com.dhflour.dhflourdemo1.core.service.jwt;

import com.dhflour.dhflourdemo1.core.types.jwt.MyUserDetails;
import com.dhflour.dhflourdemo1.core.types.jwt.UserSampleBody;
import io.jsonwebtoken.Claims;

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
