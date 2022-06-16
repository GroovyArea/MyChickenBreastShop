package com.daniel.controller.user;

import com.daniel.domain.DTO.user.UserLoginDTO;
import com.daniel.exceptions.error.UserNotExistsException;
import com.daniel.exceptions.error.WithDrawUserException;
import com.daniel.exceptions.error.WrongPasswordException;
import com.daniel.response.Message;
import com.daniel.service.RedisService;
import com.daniel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

/**
 * 로그인 처리 <br>
 * 로그인을 처리하며 jwt 토큰을 발급 후 Redis에 저장한다.
 *
 * @author 김남영
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class LoginController {

    private static final String LOGIN_MESSAGE = "Login succeed";
    private static final long VALIDATE_IN_MILLISECONDS = 1000 * 60L * 30L;

    private final UserService userService;
    private final RedisService redisService;

    /**
     * login 처리
     *
     * @param userLoginDTO 로그인 DTO
     * @return Message 응답 정보 객체
     */
    @PostMapping("/login")
    public ResponseEntity<Message> loginAction(@RequestBody UserLoginDTO userLoginDTO) throws UserNotExistsException, NoSuchAlgorithmException, WithDrawUserException, WrongPasswordException {
        userService.loginAuth(userLoginDTO);

        String jwtToken = userService.createToken(userLoginDTO);
        redisService.setDataExpire(userLoginDTO.getUserId(), jwtToken, VALIDATE_IN_MILLISECONDS);
        return ResponseEntity.ok().body(
                Message.builder()
                        .data(jwtToken)
                        .message(LOGIN_MESSAGE)
                        .build()
        );
    }
}
