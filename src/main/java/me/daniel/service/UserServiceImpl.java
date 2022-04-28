package me.daniel.service;

import me.daniel.domain.UserVO;
import me.daniel.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public void insertTest(UserVO userVO) {
        userMapper.insertTest(userVO);
    }
}
