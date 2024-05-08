package com.dhflour.dhflourdemo1.core.types.error;

public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 8087908946312578899L;

    public BadRequestException() {
        super("잘못된 요청입니다.");
    }

    public BadRequestException(String message) {
        super(message);
    }
}

