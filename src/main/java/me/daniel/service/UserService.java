package me.daniel.service;

import me.daniel.domain.DTO.UserLoginDTO;
import me.daniel.enums.users.ExceptionMessages;
import me.daniel.enums.users.UserGrade;
import me.daniel.exception.WithDrawalUserException;
import me.daniel.exception.WrongPasswordException;
import me.daniel.jwt.JwtTokenProvider;
import me.daniel.domain.DTO.UserDTO;
import me.daniel.domain.DTO.UserModifyDTO;
import me.daniel.domain.VO.UserVO;
import me.daniel.exception.LoginFailException;
import me.daniel.exception.UserExistsException;
import me.daniel.mapper.UserMapper;
import me.daniel.utility.PasswordEncrypt;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Service
public class UserService {

    private static final int WITHDRAWAL_USER_GRADE = 0;

    private final UserMapper userMapper;
    private final ModelMapper modelMapper;
    private final JwtTokenProvider jwtTokenProvider;


    public UserService(UserMapper userMapper, ModelMapper modelMapper, JwtTokenProvider jwtTokenProvider) {
        this.userMapper = userMapper;
        this.modelMapper = modelMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional(readOnly = true)
    public UserDTO findById(String userId) {
        return modelMapper.map(userMapper.selectUser(userId), UserDTO.class);
    }

    @Transactional
    public void addUser(UserDTO joinUser) throws UserExistsException, NoSuchAlgorithmException {
        if (userMapper.selectUser(joinUser.getUserId()) != null) {
            throw new UserExistsException(ExceptionMessages.USER_EXISTS_MESSAGE.getValue());
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
    public void deleteUser(Map<String, Object> map) {
        userMapper.deleteUser(map);
    }

    public void loginAuth(UserLoginDTO userLoginDTO) throws LoginFailException, NoSuchAlgorithmException, WithDrawalUserException, WrongPasswordException {
        UserVO authUser = userMapper.selectUser(userLoginDTO.getUserId());
        if (authUser == null) {
            throw new LoginFailException(ExceptionMessages.LOGIN_FAIL_MESSAGE.getValue());
        }

        if (authUser.getUserGrade() == WITHDRAWAL_USER_GRADE) {
            throw new WithDrawalUserException(ExceptionMessages.WITHDRAWAL_USER_MESSAGE.getValue());
        }

        String dbSalt = authUser.getUserSalt();
        String loginPassword = PasswordEncrypt.getSecurePassword(userLoginDTO.getUserPw(), dbSalt);
        String dbPassword = authUser.getUserPw();

        if (!loginPassword.equals(dbPassword)) {
            throw new WrongPasswordException(ExceptionMessages.WRONG_PASSWORD_MESSAGE.getValue());
        }
    }

    public String createToken(UserLoginDTO userLoginDTO) {
        return jwtTokenProvider.createToken(userLoginDTO.getUserId(), String.valueOf(UserGrade.of(userMapper.selectUser(userLoginDTO.getUserId()).getUserGrade())));
    }
}
