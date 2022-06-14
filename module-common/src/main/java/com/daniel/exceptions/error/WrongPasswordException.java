package com.daniel.exceptions.error;

public class WrongPasswordException extends Exception {

    public WrongPasswordException() {

    }

    public WrongPasswordException(String message) {
        super(message);

    }
}
