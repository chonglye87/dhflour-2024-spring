package com.dhflour.dhflourdemo1.api.service.user;

import com.dhflour.dhflourdemo1.api.domain.user.RUser;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface UserAPIService {
    // 활성 유저
    Mono<RUser> getActiveUser(String email);
    Mono<RUser> getActiveUser(Long id);

    // RefreshToken 저장 및 검증
    void saveRefreshToken(String refreshToken, ServerWebExchange exchange);
    Mono<Boolean> varifyRefreshToken(String refreshToken, ServerWebExchange exchange);
}
