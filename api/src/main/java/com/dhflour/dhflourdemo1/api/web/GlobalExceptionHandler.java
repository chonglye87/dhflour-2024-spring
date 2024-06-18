package com.dhflour.dhflourdemo1.api.web;

import com.dhflour.dhflourdemo1.api.types.error.ErrorResponse;
import com.dhflour.dhflourdemo1.core.types.error.*;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Gson gson;

    public GlobalExceptionHandler(Gson gson) {
        this.gson = gson;
    }

    @ExceptionHandler(RuntimeException.class)
    public Mono<Void> handleRuntimeException(ServerWebExchange exchange, RuntimeException ex) {
        ex.printStackTrace();
        return handleException(exchange, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    @ExceptionHandler(NullPointerException.class)
    public Mono<Void> handleNullPointerException(ServerWebExchange exchange, NullPointerException ex) {
        ex.printStackTrace();
        return handleException(exchange, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    @ExceptionHandler(Exception.class)
    public Mono<Void> handleException(ServerWebExchange exchange, Exception ex) {
        ex.printStackTrace();
        return handleException(exchange, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    @ExceptionHandler(BadRequestException.class)
    public Mono<Void> handleBadRequestException(ServerWebExchange exchange, BadRequestException ex) {
        return handleException(exchange, HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public Mono<Void> handleConflictException(ServerWebExchange exchange, ConflictException ex) {
        return handleException(exchange, HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public Mono<Void> handleNotFoundException(ServerWebExchange exchange, NotFoundException ex) {
        return handleException(exchange, HttpStatus.NOT_FOUND,ex.getMessage());
    }

    @ExceptionHandler(TooManyRequestsException.class)
    public Mono<Void> handleTooManyRequestsException(ServerWebExchange exchange, TooManyRequestsException ex) {
        return handleException(exchange, HttpStatus.TOO_MANY_REQUESTS, ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public Mono<Void> handleUnauthorizedException(ServerWebExchange exchange, UnauthorizedException ex) {
        return handleException(exchange, HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public Mono<Void> handleForbiddenException(ServerWebExchange exchange, ForbiddenException ex) {
        return handleException(exchange, HttpStatus.FORBIDDEN, ex.getMessage());
    }

    private Mono<Void> handleException(ServerWebExchange exchange, HttpStatus status, String message) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(message)
                .build();

        String jsonResponse = gson.toJson(errorResponse);

        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory()
                .wrap(jsonResponse.getBytes())));
    }
}
