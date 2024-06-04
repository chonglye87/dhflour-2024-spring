package com.dhflour.dhflourdemo1.api.config;

import com.dhflour.dhflourdemo1.api.service.userdetail.MyReactiveUserDetailsService;
import com.dhflour.dhflourdemo1.api.types.jwt.ReactiveUserDetails;
import com.dhflour.dhflourdemo1.core.service.jwt.JWTSymmetricService;
import com.dhflour.dhflourdemo1.core.types.error.UnauthorizedException;
import io.micrometer.common.util.StringUtils;
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

@Slf4j
@Component
public class JWTWebFluxRequestFilter implements WebFilter {

    @Autowired
    private JWTSymmetricService jwtService; // JWT 토큰을 관리하는 서비스

    @Autowired
    private MyReactiveUserDetailsService myReactiveUserDetailsService;

    public Boolean validateToken(String token, ReactiveUserDetails userDetails) {
        final String email = jwtService.extractSubject(token); // 토큰에서 이메일을 추출
        return (email.equals(userDetails.getEmail()) && !jwtService.isTokenExpired(token)); // 이메일이 일치하고 토큰이 만료되지 않았는지 확인
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION);
        String token = null;
        String username = "";

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtService.extractSubject(token);
            } catch (Exception e) {
                log.error("Unable to get JWT Token", e);
            }
        }

        if (token != null && StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            String finalToken = token;
            return this.myReactiveUserDetailsService.findByUsername(username)
                    .flatMap(userDetails -> {
                        ReactiveUserDetails details = (ReactiveUserDetails) userDetails;
                        log.debug("details: {}", details);
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
