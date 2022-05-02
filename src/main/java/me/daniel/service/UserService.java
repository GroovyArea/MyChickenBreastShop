package me.daniel.service;

import me.daniel.domain.UserVO;

public interface UserService {

    UserVO getUser(int userNo);

    void addUser(UserVO userVO);
}
