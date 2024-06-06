package com.dhflour.dhflourdemo1.api.config;

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

@Slf4j
@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Mono<Object> resolveArgument(MethodParameter parameter, BindingContext bindingContext, ServerWebExchange exchange) {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> {
                    Authentication authentication = context.getAuthentication();
                    log.debug("resolveArgument : {}", authentication);
                    if (authentication == null || !(authentication.getPrincipal() instanceof ReactiveUserDetails)) {
//                        throw new UnauthorizedException("Invalid or missing access token");
                        return Mono.just("Unauthorized");
                    }
                    return authentication.getPrincipal();
                }).or(Mono.empty());
    }
}
