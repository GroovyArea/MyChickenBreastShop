package me.daniel.service;

import lombok.RequiredArgsConstructor;
import me.daniel.domain.UserVO;
import me.daniel.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public UserVO getUser(int userNo) {
        return userMapper.selectUser(userNo);
    }

    @Override
    @Transactional
    public void insertTest(UserVO userVO) {
        userMapper.insertTest(userVO);
    }
}
