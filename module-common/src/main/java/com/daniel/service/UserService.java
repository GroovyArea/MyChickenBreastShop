package com.daniel.service;

import com.daniel.domain.DTO.user.*;
import com.daniel.domain.VO.UserVO;
import com.daniel.enums.users.UserGrade;
import com.daniel.exceptions.error.UserExistsException;
import com.daniel.exceptions.error.UserNotExistsException;
import com.daniel.exceptions.error.WithDrawUserException;
import com.daniel.exceptions.error.WrongPasswordException;
import com.daniel.jwt.JwtTokenProvider;
import com.daniel.mapper.UserMapper;
import com.daniel.utility.Pager;
import com.daniel.utility.PasswordEncrypt;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final String USER_EXISTS_MESSAGE = "이미 사용중인 아이디를 입력 하셨습니다.";
    private static final String LOGIN_FAIL_MESSAGE = "해당 아이디의 회원 정보가 존재하지 않습니다.";
    private static final String WITHDRAW_USER_MESSAGE = "탈퇴 회원입니다.";
    private static final String WRONG_PASSWORD_MESSAGE = "비밀번호가 일치하지 않습니다.";

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

    @Transactional(readOnly = true)
    public List<UserListDTO> getUserList(String searchKeyword, String searchValue, Pager pager) {
        return userMapper.selectUserList(searchKeyword, searchValue, pager).stream()
                .map(userVO -> modelMapper.map(userVO, UserListDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void addUser(UserJoinDTO joinUser) throws UserExistsException, NoSuchAlgorithmException {
        if (userMapper.selectUser(joinUser.getUserId()) != null) {
            throw new UserExistsException(USER_EXISTS_MESSAGE);
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
    public void changeGradeUser(Map<String, Object> map) {
        userMapper.changeGradeUser(map);
    }

    /**
     * 로그인 인증 검사
     *
     * @param userLoginDTO 로그인 회원
     * @throws UserNotExistsException   회원 정보가 존재하지 않을 시 예외
     * @throws NoSuchAlgorithmException 암호화 알고리즘 부적절 시 예외
     * @throws WithDrawUserException    탈퇴 회원일 시 예외
     * @throws WrongPasswordException   비밀번호 불일치 시 예외
     */
    public void loginAuth(UserLoginDTO userLoginDTO) throws UserNotExistsException, NoSuchAlgorithmException, WithDrawUserException, WrongPasswordException {
        UserVO authUser = userMapper.selectUser(userLoginDTO.getUserId());
        if (authUser == null) {
            throw new UserNotExistsException(LOGIN_FAIL_MESSAGE);
        }

        if (authUser.getUserGrade() == UserGrade.WITHDRAWAL_USER.getValue()) {
            throw new WithDrawUserException(WITHDRAW_USER_MESSAGE);
        }

        String dbSalt = authUser.getUserSalt();
        String loginPassword = PasswordEncrypt.getSecurePassword(userLoginDTO.getUserPw(), dbSalt);
        String dbPassword = authUser.getUserPw();

        if (!loginPassword.equals(dbPassword)) {
            throw new WrongPasswordException(WRONG_PASSWORD_MESSAGE);
        }
    }

    /**
     * 로그인 유저 토큰 생성
     *
     * @param userLoginDTO 로그인 회원
     * @return 토큰 값
     */
    public String createToken(UserLoginDTO userLoginDTO) {
        return jwtTokenProvider.createToken(userLoginDTO.getUserId(), String.valueOf(UserGrade.of(userMapper.selectUser(userLoginDTO.getUserId()).getUserGrade()).orElse(UserGrade.BASIC_USER)));
    }

}
