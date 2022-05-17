package me.daniel.exception;

public class UserExistsException extends Exception {

    public UserExistsException() {

    }

    public UserExistsException(String message) {
        super(message);
    }

}
