package me.daniel.service;

import me.daniel.domain.UserVO;
import me.daniel.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserVO getUser(int userNo) {
        return userMapper.selectUser(userNo);
    }

    @Override
    @Transactional
    public void addUser(UserVO userVO) {
        userMapper.insertUser(userVO);
    }
}
