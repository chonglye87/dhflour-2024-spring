package com.dhflour.dhflourdemo1.api.config.webflux;

import com.dhflour.dhflourdemo1.api.service.userdetail.MyReactiveUserDetailsService;
import com.dhflour.dhflourdemo1.core.service.jwt.JWTSymmetricService;
import com.dhflour.dhflourdemo1.api.types.jwt.ReactiveUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
        String username = null;
        log.debug("authHeader: {}", authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                log.debug("token: {}", token);
                username = jwtService.extractSubject(token);
            } catch (Exception e) {
                log.error("Unable to get JWT Token");
            }
        } else {
            log.error("JWT Token does not begin with Bearer String");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Mono<UserDetails> userDetails = this.myReactiveUserDetailsService.findByUsername(username);
            ReactiveUserDetails details = (ReactiveUserDetails) userDetails.blockOptional().orElseThrow();
            if (this.validateToken(token, details)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, details.getAuthorities());

                return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authenticationToken));
            }
        }

        return chain.filter(exchange);
    }
}
