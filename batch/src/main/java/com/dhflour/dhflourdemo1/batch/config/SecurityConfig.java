package com.dhflour.dhflourdemo1.batch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/**").permitAll() // `/api/**` 경로에 대한 모든 요청을 허용합니다.
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/api-docs", "/api-docs/**").permitAll() // Swagger UI와 API 문서 경로를 허용합니다.
                        .anyRequest().authenticated() // 다른 모든 요청은 인증이 필요합니다.
                )
                .securityContext(AbstractHttpConfigurer::disable) // 보안 컨텍스트 유지 기능을 비활성화합니다.
                .sessionManagement(AbstractHttpConfigurer::disable) // 세션 생성을 비활성화하여 상태를 유지하지 않습니다.
                .csrf(AbstractHttpConfigurer::disable); // CSRF 보호 기능을 명시적으로 비활성화합니다.

        // 추가 보안 기능을 필요에 따라 구성할 수 있습니다.
        return http.build();
    }
}
