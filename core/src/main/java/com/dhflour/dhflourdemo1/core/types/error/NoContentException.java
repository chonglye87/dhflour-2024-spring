package com.dhflour.dhflourdemo1.core.types.error;

public class NoContentException  extends RuntimeException {

    public NoContentException() {
        super("응답에 성공했으나, 데이터가 존재하지 않습니다.");
    }

    public NoContentException(String message) {
        super(message);
    }
}


