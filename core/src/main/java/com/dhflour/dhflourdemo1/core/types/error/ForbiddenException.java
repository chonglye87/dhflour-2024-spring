package com.dhflour.dhflourdemo1.core.types.error;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
        super("접근이 금지되었습니다. 인증 자격 증명은 있지만 권한이 부족합니다.");
    }

    public ForbiddenException(String message) {
        super(message);
    }
}

