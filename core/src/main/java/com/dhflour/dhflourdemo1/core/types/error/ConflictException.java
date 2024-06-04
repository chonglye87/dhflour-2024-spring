package com.dhflour.dhflourdemo1.core.types.error;

public class ConflictException extends RuntimeException {

    public ConflictException() {
        super("요청이 현재 서버 상태와 충돌합니다.");
    }

    public ConflictException(String message) {
        super(message);
    }
}

