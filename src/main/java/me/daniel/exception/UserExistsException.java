package me.daniel.exception;

import me.daniel.domain.DTO.UserDTO;
import me.daniel.domain.VO.UserVO;

public class UserExistsException extends Exception {

    private UserDTO user;

    public UserExistsException() {

    }

    public UserExistsException(String message, UserDTO user) {
        super(message);
        this.user = user;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
