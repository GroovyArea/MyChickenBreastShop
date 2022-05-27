package me.daniel.controller.exception;

import me.daniel.exceptions.LoginFailException;
import me.daniel.exceptions.UserExistsException;
import me.daniel.exceptions.WithDrawUserException;
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
