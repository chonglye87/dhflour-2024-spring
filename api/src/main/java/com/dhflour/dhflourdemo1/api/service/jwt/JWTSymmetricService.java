package com.dhflour.dhflourdemo1.api.service.jwt;

import com.dhflour.dhflourdemo1.core.types.jwt.UserSampleBody;
import io.jsonwebtoken.Claims;

/**
 * 대칭키 알고리즘의 JWT
 */
public interface JWTSymmetricService {

    void createSecretKey();

    String generateToken(UserSampleBody user);

    Claims verifyToken(String jwtToken);
}
