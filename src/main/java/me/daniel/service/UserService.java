package me.daniel.service;

import me.daniel.domain.UserVO;
import me.daniel.exception.UserExistsException;

public interface UserService {

    UserVO getUser(String userId);

    void addUser(UserVO userVO) throws UserExistsException;

    void modifyUser(UserVO userVO);

    void deleteUser(String userId);
}
