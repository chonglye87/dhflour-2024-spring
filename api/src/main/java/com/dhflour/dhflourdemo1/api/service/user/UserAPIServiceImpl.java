package com.dhflour.dhflourdemo1.api.service.user;

import com.dhflour.dhflourdemo1.api.domain.user.RUser;
import com.dhflour.dhflourdemo1.api.domain.user.RUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.dhflour.dhflourdemo1.core.utils.WebUtils.getClientIp;
import static com.dhflour.dhflourdemo1.core.utils.WebUtils.getDeviceDetector;

@Slf4j
@Service
public class UserAPIServiceImpl implements UserAPIService {

    @Autowired
    private RUserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Mono<RUser> getActiveUser(String email) {
        return userRepository.findOneByEmail(email); // TODO 활성 여부 체크 (탈퇴 및 잠금)
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<RUser> getActiveUser(Long id) {
        return userRepository.findById(id); // TODO 활성 여부 체크 (탈퇴 및 잠금)
    }

    @Override
    public void saveRefreshToken(String refreshToken, ServerWebExchange exchange) {
        // TODO Refresh Token 의 유효성 체크
        final String clientVersion = exchange.getRequest().getHeaders().getFirst("X-Client-Version");
        log.info("exchange.getRequest().getHeaders(): {}", exchange.getRequest().getHeaders());
        log.info("clientVersion: {}", clientVersion);
        log.info("ClientIp: {}", getClientIp(exchange));
        getDeviceDetector(exchange);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Boolean> varifyRefreshToken(String refreshToken, ServerWebExchange exchange) {
        userRepository.findById(1L);
        // TODO Refresh Token 의 유효성 체크
        final String clientVersion = exchange.getRequest().getHeaders().getFirst("X-Client-Version");
        log.info("clientVersion: {}", clientVersion);
        getDeviceDetector(exchange);
        return Mono.just(true);
    }
}
