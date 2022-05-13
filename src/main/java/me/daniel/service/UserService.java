package me.daniel.service;

import me.daniel.domain.DTO.UserDTO;
import me.daniel.domain.DTO.UserModifyDTO;
import me.daniel.domain.VO.UserVO;
import me.daniel.exception.LoginAuthFailException;
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

    private static final String USER_EXISTS_MESSAGE = "이미 사용중인 아이디를 입력 하셨습니다.";
    private static final String LOGIN_FAIL_MESSAGE = "해당 아이디의 회원 정보가 존재하지 않습니다.";
    private static final String WITHDRAWAL_USER_MESSAGE = "탈퇴 회원입니다.";
    private static final String WRONG_PASSWORD_MESSAGE = "비밀번호가 일치하지 않습니다.";
    private static final int WITHDRAWAL_USER_GRADE = 0;

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
            throw new UserExistsException(USER_EXISTS_MESSAGE, joinUser);
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

    public void loginAuth(UserDTO userDTO) throws LoginAuthFailException, NoSuchAlgorithmException {
        UserVO authUser = userMapper.selectUser(userDTO.getUserId());
        if (authUser == null) {
            throw new LoginAuthFailException(LOGIN_FAIL_MESSAGE);
        }

        if (authUser.getUserGrade() == WITHDRAWAL_USER_GRADE) {
            throw new LoginAuthFailException(WITHDRAWAL_USER_MESSAGE);
        }

        String dbSalt = authUser.getUserSalt();
        String loginPassword = PasswordEncrypt.getSecurePassword(userDTO.getUserPw(), dbSalt);
        String dbPassword = authUser.getUserPw();

        if (loginPassword.equals(dbPassword)) {
            throw new LoginAuthFailException(WRONG_PASSWORD_MESSAGE);
        }
    }
}
