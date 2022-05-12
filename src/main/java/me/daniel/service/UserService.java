package me.daniel.service;

import me.daniel.domain.DTO.UserDTO;
import me.daniel.domain.DTO.UserModifyDTO;
import me.daniel.domain.VO.UserVO;
import me.daniel.exception.UserExistsException;
import me.daniel.mapper.UserMapper;
import me.daniel.utility.PasswordEncrypt;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

@Service
public class UserService {

    private final UserMapper userMapper;

    private final ModelMapper modelMapper;

    public UserService(UserMapper userMapper, ModelMapper modelMapper) {
        this.userMapper = userMapper;
        this.modelMapper = modelMapper;
    }

    public UserDTO findById(String userId) {
        return modelMapper.map(userMapper.selectUser(userId), UserDTO.class);
    }

    @Transactional
    public void addUser(UserDTO joinUser) throws UserExistsException, NoSuchAlgorithmException {
        if (userMapper.selectUser(joinUser.getUserId()) != null) {
            throw new UserExistsException("이미 사용중인 아이디를 입력 하셨습니다.", joinUser);
        }

        String salt = PasswordEncrypt.getSalt();
        joinUser.setUserSalt(salt);
        joinUser.setUserPw(PasswordEncrypt.getSecurePassword(joinUser.getUserPw(), salt));

        userMapper.insertUser(modelMapper.map(joinUser, UserVO.class));
    }

    @Transactional
    public void modifyUser(UserModifyDTO modifyUser) {
        userMapper.updateUser(modelMapper.map(modifyUser, UserVO.class));
    }

    @Transactional
    public void deleteUser(String userId) {
        userMapper.deleteUser(userId);
    }
}
