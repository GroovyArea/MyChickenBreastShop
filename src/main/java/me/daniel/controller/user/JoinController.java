package me.daniel.controller.user;

import me.daniel.domain.DTO.UserDTO;
import me.daniel.exceptions.UserExistsException;
import me.daniel.responseMessage.Message;
import me.daniel.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/user")
public class JoinController {

    private static final String JOIN_MESSAGE = "Join succeed";

    private final UserService userService;

    public JoinController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원가입 처리
     *
     * @param joinUser 회원가입 데이터
     * @return Message 응답 정보 객체
     * @throws UserExistsException 해당 아이디를 가진 유저 존재 시 예외 발생
     * @throws NoSuchAlgorithmException 암호화 알고리즘 잘못 사용시 예외 발생
     */
    @PostMapping
    public Message joinAction(@ModelAttribute UserDTO joinUser) throws UserExistsException, NoSuchAlgorithmException {
        userService.addUser(joinUser);
        return new Message
                .Builder(userService.findById(joinUser.getUserId()))
                .message(JOIN_MESSAGE)
                .mediaType(MediaType.APPLICATION_JSON)
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
