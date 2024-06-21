package com.dhflour.dhflourdemo1.api.service.user;

import com.dhflour.dhflourdemo1.api.domain.user.RUser;
import com.dhflour.dhflourdemo1.api.domain.user.RequestSignUp;
import com.dhflour.dhflourdemo1.api.types.jwt.AuthenticationResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * 사용자 API 서비스의 주요 기능을 정의한다.
 * 활성 사용자 조회, 로그인 검증, 회원 가입, 이메일 존재 여부 확인, RefreshToken 저장 및 검증 기능을 포함한다.
 */
public interface UserAPIService {
    /**
     * 주어진 이메일에 해당하는 활성 사용자를 조회한다.
     *
     * @param email 조회할 사용자의 이메일
     * @return 조회된 활성 사용자를 포함하는 Mono<RUser>
     */
    Mono<RUser> getActiveUser(String email);

    /**
     * 주어진 ID에 해당하는 활성 사용자를 조회한다.
     *
     * @param id 조회할 사용자의 ID
     * @return 조회된 활성 사용자를 포함하는 Mono<RUser>
     */
    Mono<RUser> getActiveUser(Long id);

    /**
     * 사용자 인증 정보를 검증하고 인증 응답을 반환한다.
     *
     * @param authentication 인증 정보
     * @param exchange ServerWebExchange 객체
     * @return 인증 응답을 포함하는 Mono<AuthenticationResponse>
     */
    Mono<AuthenticationResponse> authenticate(Authentication authentication, ServerWebExchange exchange);

    /**
     * 새로운 사용자를 회원 가입시킨다.
     *
     * @param body 회원 가입 정보
     * @return 생성된 사용자를 포함하는 Mono<RUser>
     */
    Mono<RUser> signUp(RequestSignUp body);

    /**
     * 주어진 이메일이 존재하는지 확인한다.
     *
     * @param email 확인할 이메일
     * @return 이메일 존재 여부를 포함하는 Mono<Boolean>
     */
    Mono<Boolean> exist(String email);

    /**
     * RefreshToken을 저장한다.
     *
     * @param refreshToken 저장할 RefreshToken
     * @param exchange ServerWebExchange 객체
     */
    void saveRefreshToken(String refreshToken, ServerWebExchange exchange);

    /**
     * 주어진 RefreshToken을 검증한다.
     *
     * @param refreshToken 검증할 RefreshToken
     * @param exchange ServerWebExchange 객체
     * @return RefreshToken 검증 결과를 포함하는 Mono<Boolean>
     */
    Mono<Boolean> varifyRefreshToken(String refreshToken, ServerWebExchange exchange);
}