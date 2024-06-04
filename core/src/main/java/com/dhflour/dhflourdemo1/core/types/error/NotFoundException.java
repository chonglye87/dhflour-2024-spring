package com.dhflour.dhflourdemo1.core.types.error;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("요청한 리소스를 찾을 수 없습니다.");
    }

    public NotFoundException(String message) {
        super(message);
    }
}

