package com.dhflour.dhflourdemo1.api.utils;

import com.dhflour.dhflourdemo1.api.types.jwt.ReactiveUserDetails;
import com.dhflour.dhflourdemo1.core.types.error.UnauthorizedException;
import reactor.core.publisher.Mono;

public class AuthUtils {

    /**
     * 로그인이 필수일때 사용
     *
     * @param userDetails 로그인 Principal
     * @return Mono<ReactiveUserDetails>
     */
    public static Mono<ReactiveUserDetails> required(Mono<ReactiveUserDetails> userDetails) {
        if (userDetails == null) {
            return Mono.error(new UnauthorizedException("Invalid or missing access token"));
        }
        return userDetails.switchIfEmpty(Mono.error(new UnauthorizedException("Invalid or missing access token")));
    }

    /**
     * 로그인이 옵션일때 사용
     *
     * @param userDetails 로그인 Principal
     * @return Mono<ReactiveUserDetails>
     */
    public static Mono<ReactiveUserDetails> optional(Mono<ReactiveUserDetails> userDetails) {
        if (userDetails == null) {
            return Mono.empty();
        }
        return userDetails;
    }

    /**
     * 권한 체크
     *
     * @param userDetails 로그인 정보
     * @param role        역할(권한)
     * @return 결과
     */
    public static boolean hasRole(ReactiveUserDetails userDetails, String role) {
        return userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(role));
    }
}
