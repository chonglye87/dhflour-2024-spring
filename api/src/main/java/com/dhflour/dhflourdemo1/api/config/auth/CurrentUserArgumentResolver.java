package com.dhflour.dhflourdemo1.api.config.auth;

import com.dhflour.dhflourdemo1.api.types.jwt.ReactiveUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * CurrentUserArgumentResolver 클래스는 컨트롤러 메서드 인자에 현재 사용자의 정보를 주입하기 위해 사용된다.
 */
@Slf4j
@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 이 메서드는 주어진 메서드 파라미터가 CurrentUser 어노테이션을 가지고 있는지 확인한다.
     * @param parameter MethodParameter 객체
     * @return 파라미터가 CurrentUser 어노테이션을 가지고 있으면 true, 그렇지 않으면 false
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    /**
     * 이 메서드는 ReactiveSecurityContextHolder에서 현재 사용자의 인증 정보를 가져와
     * 해당 정보를 컨트롤러 메서드 인자에 주입한다.
     * @param parameter MethodParameter 객체
     * @param bindingContext BindingContext 객체
     * @param exchange ServerWebExchange 객체
     * @return 현재 사용자 인증 정보를 포함하는 Mono 객체
     */
    @Override
    public Mono<Object> resolveArgument(MethodParameter parameter, BindingContext bindingContext, ServerWebExchange exchange) {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> {
                    Authentication authentication = context.getAuthentication();
                    if (authentication == null || !(authentication.getPrincipal() instanceof ReactiveUserDetails)) {
                        // 인증 정보가 없거나 유효하지 않은 경우
                        return Mono.just("Unauthorized");
                    }
                    // 인증 정보가 유효한 경우
                    return authentication.getPrincipal();
                }).or(Mono.empty());
    }
}

