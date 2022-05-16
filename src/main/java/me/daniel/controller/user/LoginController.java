package me.daniel.controller.user;

import me.daniel.domain.DTO.UserLoginDTO;
import me.daniel.enums.ResponseMessage;
import me.daniel.exception.LoginAuthFailException;
import me.daniel.responseMessage.Message;
import me.daniel.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    /**
     * login 처리
     *
     * @param userLoginDTO 로그인 DTO
     * @return ResponseEntity 로그인 성공 메시지
     */
    @PostMapping("/user/login")
    public ResponseEntity loginAction(@ModelAttribute UserLoginDTO userLoginDTO) throws LoginAuthFailException, NoSuchAlgorithmException {
        userService.loginAuth(userLoginDTO);
        Message message = new Message
                .Builder(userService.createToken(userLoginDTO))
                .message(ResponseMessage.LOGIN_MESSAGE.getValue())
                .build();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(message);
    }
}
