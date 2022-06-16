package com.daniel.exceptions.handler;

import com.daniel.exceptions.error.UserNotExistsException;
import com.daniel.exceptions.error.UserExistsException;
import com.daniel.exceptions.error.WithDrawUserException;
import com.daniel.exceptions.error.WrongPasswordException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LoginExceptionController {

    @ExceptionHandler(value = UserNotExistsException.class)
    public ResponseEntity<String> loginFailHandler(UserNotExistsException exception) {
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

    @ExceptionHandler(value = WrongPasswordException.class)
    public ResponseEntity<String> wrongPasswordHandler(WrongPasswordException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
