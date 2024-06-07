package com.dhflour.dhflourdemo1.core.types.error;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("인증되지 않았습니다. 유효한 인증 자격 증명이 필요합니다.");
    }

    public UnauthorizedException(String message) {
        super(message);
    }


}

