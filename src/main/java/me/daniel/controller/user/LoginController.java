package me.daniel.controller.user;

import me.daniel.domain.DTO.UserLoginDTO;
import me.daniel.enums.users.ExceptionMessages;
import me.daniel.exception.LoginFailException;
import me.daniel.exception.UserExistsException;
import me.daniel.exception.WithDrawalUserException;
import me.daniel.exception.WrongPasswordException;
import me.daniel.responseMessage.Message;
import me.daniel.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

/**
 * 로그인 처리 <br>
 * 로그인을 처리하며 jwt 토큰을 발급 후 Redis에 저장한다.
 *
 * @author 김남영
 */
@RestController
@RequestMapping("/api")
public class LoginController {

    private static final String LOGIN_MESSAGE = "Login succeed";

    private final UserService userService;
    private final RedisTemplate<String, Object> redisTemplate;

    public LoginController(UserService userService, RedisTemplate<String, Object> redisTemplate) {
        this.userService = userService;
        this.redisTemplate = redisTemplate;
    }

    /**
     * login 처리
     *
     * @param userLoginDTO 로그인 DTO
     * @return Message 응답 정보 객체
     */
    @PostMapping("/user/login")
    public Message loginAction(@ModelAttribute UserLoginDTO userLoginDTO) throws LoginFailException, NoSuchAlgorithmException, WithDrawalUserException, WrongPasswordException {
        userService.loginAuth(userLoginDTO);
        String jwtToken = userService.createToken(userLoginDTO);
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(userLoginDTO.getUserId(), jwtToken);

        return new Message
                .Builder(jwtToken)
                .message(LOGIN_MESSAGE)
                .mediaType(MediaType.APPLICATION_JSON)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @ExceptionHandler(value = LoginFailException.class)
    public Message loginFailHandler() {
        return new Message
                .Builder(ExceptionMessages.LOGIN_FAIL_MESSAGE.getValue())
                .httpStatus(HttpStatus.NO_CONTENT)
                .build();
    }

    @ExceptionHandler(value = WithDrawalUserException.class)
    public Message withDrawUserHandler() {
        return new Message
                .Builder(ExceptionMessages.WITHDRAWAL_USER_MESSAGE.getValue())
                .httpStatus(HttpStatus.NO_CONTENT)
                .build();
    }

    @ExceptionHandler(value = UserExistsException.class)
    public Message userExistsHandler() {
        return new Message
                .Builder(ExceptionMessages.USER_EXISTS_MESSAGE.getValue())
                .httpStatus(HttpStatus.NO_CONTENT)
                .build();
    }

}
