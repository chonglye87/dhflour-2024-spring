package com.dhflour.dhflourdemo1.core.types.error;

public class TooManyRequestsException extends RuntimeException {

    public TooManyRequestsException() {
        super("너무 많은 요청을 보냈습니다. 나중에 다시 시도하십시오.");
    }

    public TooManyRequestsException(String message) {
        super(message);
    }
}

