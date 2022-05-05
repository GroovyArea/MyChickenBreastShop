package me.daniel.exception;

import me.daniel.domain.UserVO;

public class UserExistsException extends Exception {

    private UserVO user;

    public UserExistsException() {

    }

    public UserExistsException(String message, UserVO user) {
        super(message);
        this.user = user;
    }

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }
}
