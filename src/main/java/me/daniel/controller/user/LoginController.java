package me.daniel.controller.user;

import me.daniel.domain.DTO.UserLoginDTO;
import me.daniel.enums.users.ExceptionMessages;
import me.daniel.exception.LoginFailException;
import me.daniel.exception.UserExistsException;
import me.daniel.exception.WithDrawalUserException;
import me.daniel.exception.WrongPasswordException;
import me.daniel.responseMessage.Message;
import me.daniel.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api")
public class LoginController {

    private static final String LOGIN_MESSAGE = "Login succeed";

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
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
        return new Message
                .Builder(userService.createToken(userLoginDTO))
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
    public Message withDrawalUserHandler() {
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
