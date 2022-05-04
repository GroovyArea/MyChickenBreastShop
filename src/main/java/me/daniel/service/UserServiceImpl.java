package me.daniel.service;

import me.daniel.domain.UserVO;
import me.daniel.exception.UserExistsException;
import me.daniel.mapper.UserMapper;
import me.daniel.utility.Encrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserVO getUser(String userId) {
        return userMapper.selectUser(userId);
    }

    @Override
    @Transactional
    public void addUser(UserVO userVO) throws UserExistsException {
        if (userMapper.selectUser(userVO.getUserId()) != null) {
            throw new UserExistsException("이미 사용중인 아이디를 입력 하셨습니다.", userVO);
        }

        userVO.setUserPw(Encrypt.ecrypt(userVO.getUserPw()));
        userMapper.insertUser(userVO);
    }

    @Override
    @Transactional
    public void modifyUser(UserVO userVO) {
        userMapper.updateUser(userVO);
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {

    }
}
