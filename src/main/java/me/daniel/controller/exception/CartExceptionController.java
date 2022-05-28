package me.daniel.controller.exception;

import me.daniel.exceptions.EmptyCookiesException;
import me.daniel.exceptions.InvalidPayAmountException;
import me.daniel.exceptions.InvalidProductException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.UnsupportedEncodingException;

/**
 * 장바구니 관련 예외 핸들러 클래스 <br>
 * 장바구니 로직 관련 핸들러 처리 중 발생 된 예외를 처리한다.
 */
@RestControllerAdvice
public class CartExceptionController {

    private static final String UNSUPPORTED_ENCODING = "인코딩을 지원하지 않는 형식의 쿠키입니다.";

    @ExceptionHandler(UnsupportedEncodingException.class)
    public ResponseEntity<String> unsupportedEncodingException() {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(UNSUPPORTED_ENCODING);
    }

    @ExceptionHandler(EmptyCookiesException.class)
    public ResponseEntity<String> emptyCookiesException(EmptyCookiesException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(e.getMessage());
    }

    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<String> invalidProductException(InvalidProductException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(InvalidPayAmountException.class)
    public ResponseEntity<String> invalidPayAmountException(InvalidPayAmountException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}
