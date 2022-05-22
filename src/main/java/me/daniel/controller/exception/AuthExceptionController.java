package me.daniel.controller.exception;

import me.daniel.exceptions.RedisNullTokenException;
import me.daniel.exceptions.TokenMismatchException;
import me.daniel.interceptor.auth.AuthMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.sasl.AuthenticationException;

/**
 * 권한 처리 예외 핸들러 클래스 <br>
 * 권한 처리 중 발생 된 예외를 처리한다.
 */
@RestControllerAdvice
public class AuthExceptionController {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> authException() {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(AuthMessages.NOT_ADMIN_AUTH.getMessage());
    }

    @ExceptionHandler(RedisNullTokenException.class)
    public ResponseEntity<String> nullTokenException() {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(AuthMessages.NULL_TOKEN.getMessage());
    }

    @ExceptionHandler(TokenMismatchException.class)
    public ResponseEntity<String> tokenMissMatchException() {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(AuthMessages.INVALID_TOKEN.getMessage());
    }
}
