package com.daniel.exceptions.error;

public class UserNotExistsException extends Exception {

    public UserNotExistsException() {

    }

    public UserNotExistsException(String message) {
        super(message);

    }
}
