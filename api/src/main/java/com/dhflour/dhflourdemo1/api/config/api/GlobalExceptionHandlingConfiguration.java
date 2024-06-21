package com.dhflour.dhflourdemo1.api.config.api;


import com.dhflour.dhflourdemo1.api.web.GlobalExceptionHandler;
import com.dhflour.dhflourdemo1.core.types.error.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * GlobalExceptionHandlingConfiguration 클래스는 전역 예외 처리를 위한 설정을 담당한다.
 * 다양한 예외 상황에 대해 적절한 핸들러를 호출하여 응답을 처리한다.
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandlingConfiguration implements WebExceptionHandler {

    private final GlobalExceptionHandler globalExceptionHandler;

    // 예외 타입별로 처리할 핸들러를 매핑하는 맵
    private final Map<Class<? extends Throwable>, BiFunction<ServerWebExchange, Throwable, Mono<Void>>> exceptionHandlers = new HashMap<>();

    /**
     * 생성자에서 GlobalExceptionHandler를 주입받고, 예외 핸들러 맵을 초기화한다.
     * @param globalExceptionHandler 전역 예외 핸들러
     */
    public GlobalExceptionHandlingConfiguration(GlobalExceptionHandler globalExceptionHandler) {
        this.globalExceptionHandler = globalExceptionHandler;
        initializeExceptionHandlers();
    }

    /**
     * 예외 타입별로 해당 예외를 처리할 핸들러를 등록한다.
     */
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

    /**
     * 발생한 예외를 적절한 핸들러로 처리한다.
     * @param exchange ServerWebExchange 객체
     * @param ex 발생한 예외
     * @return 처리 결과를 나타내는 Mono<Void>
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        BiFunction<ServerWebExchange, Throwable, Mono<Void>> handler = exceptionHandlers.getOrDefault(ex.getClass(), (e, t) -> globalExceptionHandler.handleException(e, (Exception) t));
        return handler.apply(exchange, ex);
    }
}