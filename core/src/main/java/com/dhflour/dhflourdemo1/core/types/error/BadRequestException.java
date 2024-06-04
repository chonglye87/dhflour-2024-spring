package com.dhflour.dhflourdemo1.core.types.error;

public class BadRequestException extends RuntimeException {

    public BadRequestException() {
        super("잘못된 요청입니다. 요청 구문, 매개변수 등이 잘못되었습니다.");
    }

    public BadRequestException(String message) {
        super(message);
    }
}

