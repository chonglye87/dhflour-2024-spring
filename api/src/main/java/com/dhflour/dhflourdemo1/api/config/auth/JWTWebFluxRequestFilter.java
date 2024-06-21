package com.dhflour.dhflourdemo1.api.config.auth;

import com.dhflour.dhflourdemo1.api.service.userdetail.MyReactiveUserDetailsService;
import com.dhflour.dhflourdemo1.api.types.jwt.ReactiveUserDetails;
import com.dhflour.dhflourdemo1.core.service.jwt.JWTSymmetricService;
import com.dhflour.dhflourdemo1.core.types.error.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * JWTWebFluxRequestFilter 클래스는 JWT 토큰을 사용한 인증을 처리하기 위한 웹 필터
 */
@Slf4j
@Component
public class JWTWebFluxRequestFilter implements WebFilter {

    @Autowired
    private JWTSymmetricService jwtService; // JWT 토큰을 관리하는 서비스

    @Autowired
    private MyReactiveUserDetailsService userDetailsService;

    /**
     * JWT 토큰이 유효한지 확인하는 메서드.
     * @param token JWT 토큰
     * @param userDetails 사용자 정보
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     */
    public Boolean validateToken(String token, ReactiveUserDetails userDetails) {
        // final String email = jwtService.extractSubject(token); // 토큰에서 이메일을 추출
        final Long id = jwtService.extractId(token); // 토큰에서 ID 추출
        return (id.equals(userDetails.getId()) && !jwtService.isTokenExpired(token)); // 이메일이 일치하고 토큰이 만료되지 않았는지 확인
    }

    /**
     * JWT 토큰을 사용하여 요청을 필터링하고, 인증 정보를 설정하는 메서드.
     * @param exchange ServerWebExchange 객체
     * @param chain WebFilterChain 객체
     * @return 처리 결과
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION);
        String token = null;
        Long id = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                id = jwtService.extractId(token);
            } catch (Exception e) {
                log.error("Unable to get JWT Token", e);
            }
        }

        if (token != null && id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String finalToken = token;
            return userDetailsService.findByUsername(String.valueOf(id))
                    .flatMap(userDetails -> {
                        ReactiveUserDetails details = (ReactiveUserDetails) userDetails;
                        if (this.validateToken(finalToken, details)) {
                            UsernamePasswordAuthenticationToken authenticationToken =
                                    new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());

                            return chain.filter(exchange)
                                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authenticationToken));
                        } else {
                            return Mono.error(new UnauthorizedException("Invalid JWT token"));
                        }
                    })
                    .onErrorResume(e -> {
                        log.error("Authentication error", e);
                        return chain.filter(exchange);
                    });
        }
        return chain.filter(exchange);
    }
}