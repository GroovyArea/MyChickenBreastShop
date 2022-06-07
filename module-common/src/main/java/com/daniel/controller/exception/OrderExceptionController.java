package com.daniel.controller.exception;

import com.daniel.exceptions.RunOutOfStockException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 주문 관련 예외 핸들러 클래스 <br>
 * 주문 로직 관련 핸들러 처리 중 발생 된 예외를 처리한다.
 */
@RestControllerAdvice
public class OrderExceptionController {

    @ExceptionHandler(RunOutOfStockException.class)
    public ResponseEntity<String> runOutOfException(RunOutOfStockException e) {
        return ResponseEntity.ok().body(e.getMessage());
    }
}
