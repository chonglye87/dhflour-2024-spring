package com.dhflour.dhflourdemo1.api.service.user;

import com.dhflour.dhflourdemo1.api.domain.user.RUser;
import com.dhflour.dhflourdemo1.api.domain.user.RequestSignUp;
import com.dhflour.dhflourdemo1.api.types.jwt.AuthenticationResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface UserAPIService {
    // 활성 유저
    Mono<RUser> getActiveUser(String email);
    Mono<RUser> getActiveUser(Long id);

    // 로그인 검증
    Mono<AuthenticationResponse> authenticate(Authentication authentication, ServerWebExchange exchange);

    // 회원 가입
    Mono<RUser> signUp(RequestSignUp body);
    Mono<Boolean> exist(String email);

    // RefreshToken 저장 및 검증
    void saveRefreshToken(String refreshToken, ServerWebExchange exchange);
    Mono<Boolean> varifyRefreshToken(String refreshToken, ServerWebExchange exchange);
}
