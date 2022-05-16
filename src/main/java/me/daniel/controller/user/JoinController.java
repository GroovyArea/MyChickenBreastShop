package me.daniel.controller.user;

import jdk.jfr.ContentType;
import me.daniel.domain.DTO.UserDTO;
import me.daniel.enums.ResponseMessage;
import me.daniel.exception.UserExistsException;
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
@RequestMapping("/user")
public class JoinController {

    private final UserService userService;

    public JoinController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원가입 처리
     *
     * @param joinUser 회원가입 데이터
     * @return ResponseEntity 응답 헤더 바디
     * @throws UserExistsException 해당 아이디를 가진 유저 존재 시 예외 발생
     */
    @PostMapping()
    public ResponseEntity<Message> joinAction(@ModelAttribute UserDTO joinUser) throws UserExistsException, NoSuchAlgorithmException {
        userService.addUser(joinUser);
        Message message = new Message
                .Builder(userService.findById(joinUser.getUserId()))
                .message(ResponseMessage.JOIN_MESSAGE.getValue())
                .build();
        return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(message);
    }
}
