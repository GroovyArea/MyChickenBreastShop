package com.daniel.exceptions.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestControllerAdvice
public class FileExceptionController {

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<String> fileNotFountExceptionHandler(FileNotFoundException e) {
        return  ResponseEntity.badRequest().body(e.getMessage());
    }


    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> runtimeExceptionHandler(IOException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
