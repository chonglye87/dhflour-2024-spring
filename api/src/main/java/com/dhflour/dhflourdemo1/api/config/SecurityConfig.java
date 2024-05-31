package com.dhflour.dhflourdemo1.api.config;

import com.dhflour.dhflourdemo1.core.service.userdetail.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private MyUserDetailsService myUserDetailsService; // 사용자 세부 정보를 제공하는 서비스

    @Autowired
    private JwtRequestFilter jwtRequestFilter; // JWT 요청 필터

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/v1/board/**").permitAll() // `/api/v1/board/**` 경로에 대한 모든 요청을 허용합니다.
                        .requestMatchers("/api/v1/user/**").permitAll() // `/api/v1/user/**` 경로에 대한 모든 요청을 허용합니다.
                        .requestMatchers("/api/v1/category/**").permitAll() // `/api/v1/category/**` 경로에 대한 모든 요청을 허용합니다.
                        .requestMatchers("/api/v1/authenticate").permitAll() // 로그인 요청을 허용합니다.
                        .requestMatchers("/actuator/**").permitAll() // `/actuator/**` 경로에 대한 모든 요청을 허용합니다.
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/api-docs", "/api-docs/**").permitAll() // Swagger UI와 API 문서 경로를 허용합니다.
                        .anyRequest().authenticated() // 다른 모든 요청은 인증이 필요합니다.
                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 관리를 상태 비저장 방식으로 설정합니다.
                )
                .securityContext(AbstractHttpConfigurer::disable) // 보안 컨텍스트 유지 기능을 비활성화합니다.
                .sessionManagement(AbstractHttpConfigurer::disable) // 세션 생성을 비활성화하여 상태를 유지하지 않습니다.
                .csrf(AbstractHttpConfigurer::disable); // CSRF 보호 기능을 명시적으로 비활성화합니다.

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // JWT 요청 필터를 UsernamePasswordAuthenticationFilter 앞에 추가합니다.
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 인코더로 BCryptPasswordEncoder를 사용합니다.
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // AuthenticationManager 빈을 생성합니다.
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(myUserDetailsService); // 사용자 세부 정보를 설정합니다.
        provider.setPasswordEncoder(passwordEncoder()); // 비밀번호 인코더를 설정합니다.
        return provider;
    }
}
