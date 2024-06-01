package com.dhflour.dhflourdemo1.api.config;

import com.dhflour.dhflourdemo1.core.service.jwt.JWTSymmetricService;
import com.dhflour.dhflourdemo1.core.types.jwt.MyUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService; // 유저 정보를 로드하는 서비스

    @Autowired
    private JWTSymmetricService jwtService; // JWT 토큰을 관리하는 서비스

    // 토큰을 검증하는 메서드
    public Boolean validateToken(String token, MyUserDetails userDetails) {
        final String email = jwtService.extractSubject(token); // 토큰에서 이메일을 추출
        return (email.equals(userDetails.getEmail()) && !jwtService.isTokenExpired(token)); // 이메일이 일치하고 토큰이 만료되지 않았는지 확인
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization"); // 요청 헤더에서 Authorization을 가져옴
        String username = null;
        String jwt = null;

        // 헤더가 "Bearer "로 시작하는지 확인
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 부분만 추출
            try {
                username = jwtService.extractSubject(jwt); // 토큰에서 사용자 이름(이메일)을 추출
            } catch (ExpiredJwtException e) {
                // 토큰이 만료된 경우 처리
                e.printStackTrace();
            }
        }
        // 사용자 이름이 있고, 인증이 되어 있지 않은 경우
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            MyUserDetails userDetails = (MyUserDetails) this.userDetailsService.loadUserByUsername(username); // 사용자 정보를 로드
            if (this.validateToken(jwt, userDetails)) { // 토큰이 유효한지 확인
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // 인증 객체 생성
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // 요청 세부 정보를 설정

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken); // 인증 객체를 보안 컨텍스트에 설정
                // >> @AuthenticationPrincipal MyUserDetails userDetails 사용가능해짐.
            }
        }
        chain.doFilter(request, response); // 다음 필터 체인으로 요청과 응답을 전달
    }
}
