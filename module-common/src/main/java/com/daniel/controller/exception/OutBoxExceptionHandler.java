package com.daniel.controller.exception;

import com.daniel.exceptions.FailedPayloadConvertException;
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
