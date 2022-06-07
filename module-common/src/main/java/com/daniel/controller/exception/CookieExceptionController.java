package com.daniel.controller.exception;

import com.daniel.exceptions.EmptyCookiesException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CookieExceptionController {

    private static final String EMPTY_COOKIE = "쿠키가 없습니다.";

    @ExceptionHandler(EmptyCookiesException.class)
    public ResponseEntity<String> emptyCookiesException() {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(EMPTY_COOKIE);
    }
}
