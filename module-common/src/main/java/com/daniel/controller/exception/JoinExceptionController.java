package com.daniel.controller.exception;

import com.daniel.exceptions.EmailAuthException;
import com.daniel.exceptions.UserExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 회원 가입 예외 핸들러 클래스 <br>
 *
 * <pre>
 *     <b>History</b>
 *     김남영, 1.0, 2022.05.26 최초 작성
 * </pre>
 *
 * @author 김남영
 * @version 1.0
 */
@RestControllerAdvice
public class JoinExceptionController {

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<String> userExistsException(UserExistsException exception) {
        return ResponseEntity
                .badRequest()
                .body(exception.getMessage());
    }

    @ExceptionHandler(EmailAuthException.class)
    public ResponseEntity<String> emailAuthException(EmailAuthException exception) {
        return ResponseEntity
                .badRequest()
                .body(exception.getMessage());
    }
}
