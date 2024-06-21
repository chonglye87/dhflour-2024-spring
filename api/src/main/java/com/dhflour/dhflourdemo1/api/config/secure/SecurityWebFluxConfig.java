package com.dhflour.dhflourdemo1.api.config.secure;

import com.dhflour.dhflourdemo1.api.config.auth.JWTWebFluxRequestFilter;
import com.dhflour.dhflourdemo1.api.service.userdetail.MyReactiveUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * SecurityWebFluxConfig 클래스는 웹플럭스 보안 설정을 담당한다.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityWebFluxConfig {

    @Autowired
    private MyReactiveUserDetailsService myReactiveUserDetailsService;

    @Autowired
    private JWTWebFluxRequestFilter jwtRequestFilter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 보안 필터 체인을 구성하는 메서드.
     * 특정 경로에 대한 접근 권한을 설정하고, JWT 필터를 추가한다.
     *
     * @param http ServerHttpSecurity 객체
     * @return 구성된 SecurityWebFilterChain 객체
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/v1/board/**").permitAll()
                        .pathMatchers("/api/v1/category/**").permitAll()
                        .pathMatchers("/api/v1/authenticate").permitAll()
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/api-docs", "/api-docs/**").permitAll()
                        .anyExchange().permitAll() // 모든 요청을 허용한다.
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable); // CSRF 보호를 비활성화한다.

        http.addFilterAt(jwtRequestFilter, SecurityWebFiltersOrder.AUTHENTICATION); // JWT 필터를 인증 필터 위치에 추가한다.
        return http.build();
    }

    /**
     * 반응형 인증 매니저를 구성하는 메서드.
     * 사용자 세부 정보 서비스와 비밀번호 인코더를 설정한다.
     *
     * @return 구성된 UserDetailsRepositoryReactiveAuthenticationManager 객체
     */
    @Bean
    public UserDetailsRepositoryReactiveAuthenticationManager reactiveAuthenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(myReactiveUserDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }
}