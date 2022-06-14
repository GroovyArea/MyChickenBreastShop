package com.daniel.exceptions.handler;

import com.daniel.exceptions.error.FailedPayloadConvertException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 아웃 박스 관련 예외 핸들러
 */
@RestControllerAdvice
public class OutBoxExceptionHandler {

    @ExceptionHandler(FailedPayloadConvertException.class)
    public ResponseEntity<String> failedPayloadConvertExceptionHandle(FailedPayloadConvertException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
