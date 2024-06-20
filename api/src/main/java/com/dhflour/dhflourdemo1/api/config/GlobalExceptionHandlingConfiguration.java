package com.dhflour.dhflourdemo1.api.config;


import com.dhflour.dhflourdemo1.api.web.GlobalExceptionHandler;
import com.dhflour.dhflourdemo1.core.types.error.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandlingConfiguration implements WebExceptionHandler {

    private final GlobalExceptionHandler globalExceptionHandler;

    private final Map<Class<? extends Throwable>, BiFunction<ServerWebExchange, Throwable, Mono<Void>>> exceptionHandlers = new HashMap<>();

    public GlobalExceptionHandlingConfiguration(GlobalExceptionHandler globalExceptionHandler) {
        this.globalExceptionHandler = globalExceptionHandler;
        initializeExceptionHandlers();
    }

    private void initializeExceptionHandlers() {
        exceptionHandlers.put(RuntimeException.class, (exchange, ex) -> globalExceptionHandler.handleRuntimeException(exchange, (RuntimeException) ex));
        exceptionHandlers.put(NullPointerException.class, (exchange, ex) -> globalExceptionHandler.handleNullPointerException(exchange, (NullPointerException) ex));
        exceptionHandlers.put(BadRequestException.class, (exchange, ex) -> globalExceptionHandler.handleBadRequestException(exchange, (BadRequestException) ex));
        exceptionHandlers.put(ConflictException.class, (exchange, ex) -> globalExceptionHandler.handleConflictException(exchange, (ConflictException) ex));
        exceptionHandlers.put(NotFoundException.class, (exchange, ex) -> globalExceptionHandler.handleNotFoundException(exchange, (NotFoundException) ex));
        exceptionHandlers.put(TooManyRequestsException.class, (exchange, ex) -> globalExceptionHandler.handleTooManyRequestsException(exchange, (TooManyRequestsException) ex));
        exceptionHandlers.put(UnauthorizedException.class, (exchange, ex) -> globalExceptionHandler.handleUnauthorizedException(exchange, (UnauthorizedException) ex));
        exceptionHandlers.put(BadCredentialsException.class, (exchange, ex) -> globalExceptionHandler.handleBadCredentialsException(exchange, (BadCredentialsException) ex));
        exceptionHandlers.put(ForbiddenException.class, (exchange, ex) -> globalExceptionHandler.handleForbiddenException(exchange, (ForbiddenException) ex));
        exceptionHandlers.put(NoContentException.class, (exchange, ex) -> globalExceptionHandler.handleNoContentException(exchange, (NoContentException) ex));
        exceptionHandlers.put(Exception.class, (exchange, ex) -> globalExceptionHandler.handleException(exchange, (Exception) ex));
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        BiFunction<ServerWebExchange, Throwable, Mono<Void>> handler = exceptionHandlers.getOrDefault(ex.getClass(), (e, t) -> globalExceptionHandler.handleException(e, (Exception) t));
        return handler.apply(exchange, ex);
    }
}