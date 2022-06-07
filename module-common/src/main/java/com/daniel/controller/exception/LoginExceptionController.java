package com.daniel.controller.exception;

import com.daniel.exceptions.LoginFailException;
import com.daniel.exceptions.UserExistsException;
import com.daniel.exceptions.WithDrawUserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LoginExceptionController {

    @ExceptionHandler(value = LoginFailException.class)
    public ResponseEntity<String> loginFailHandler(LoginFailException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(value = WithDrawUserException.class)
    public ResponseEntity<String> withDrawUserHandler(WithDrawUserException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(value = UserExistsException.class)
    public ResponseEntity<String> userExistsHandler(UserExistsException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
