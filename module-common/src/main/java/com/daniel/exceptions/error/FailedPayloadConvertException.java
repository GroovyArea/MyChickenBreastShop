package com.daniel.exceptions.error;

public class FailedPayloadConvertException extends Exception {
    public FailedPayloadConvertException() {
        super("payload 변환에서 에러가 발생했습니다.");
    }
}
