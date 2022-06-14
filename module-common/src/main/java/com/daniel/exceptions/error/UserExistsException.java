package com.daniel.exceptions.error;

public class UserExistsException extends Exception {

    public UserExistsException() {

    }

    public UserExistsException(String message) {
        super(message);
    }

}
