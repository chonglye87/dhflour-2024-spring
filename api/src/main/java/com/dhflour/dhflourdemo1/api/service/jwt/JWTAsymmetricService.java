package com.dhflour.dhflourdemo1.api.service.jwt;

import com.dhflour.dhflourdemo1.core.types.jwt.UserSampleBody;
import io.jsonwebtoken.Claims;

/**
 * 비대칭키 알고리즘의 JWT
 */
public interface JWTAsymmetricService {

    void createSecretKey();

    String generateToken(UserSampleBody user);

    Claims verifyToken(String jwtToken);
}
