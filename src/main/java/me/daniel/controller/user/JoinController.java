package me.daniel.controller.user;

import me.daniel.domain.DTO.user.UserEmailRequestDTO;
import me.daniel.domain.DTO.user.UserJoinDTO;
import me.daniel.exceptions.EmailAuthException;
import me.daniel.exceptions.UserExistsException;
import me.daniel.responseMessage.Message;
import me.daniel.service.EmailService;
import me.daniel.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/user")
public class JoinController {

    private static final String JOIN_MESSAGE = "Join succeed";

    private static final Logger log = LoggerFactory.getLogger(JoinController.class);

    private final UserService userService;
    private final EmailService emailService;

    public JoinController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    /**
     * 회원가입 처리
     *
     * @param joinUser 회원가입 데이터
     * @return Message 응답 정보 객체
     * @throws UserExistsException      해당 아이디를 가진 유저 존재 시 예외 발생
     * @throws NoSuchAlgorithmException 암호화 알고리즘 잘못 사용시 예외 발생
     */
    @PostMapping
    public Message joinAction(@RequestBody UserJoinDTO joinUser) throws UserExistsException, NoSuchAlgorithmException, EmailAuthException {
        emailService.authEmail(joinUser);
        userService.addUser(joinUser);
        return new Message
                .Builder(userService.findById(joinUser.getUserId()))
                .message(JOIN_MESSAGE)
                .mediaType(MediaType.APPLICATION_JSON)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    /**
     * 이메일 인증 번호 전송 처리
     *
     * @param emailRequestDTO 이메일
     * @return 상태코드 200
     */
    @PostMapping("/email")
    public ResponseEntity<String> authEmail(@RequestBody UserEmailRequestDTO emailRequestDTO) {
        emailService.sendEmail(emailRequestDTO);
        return ResponseEntity.ok().build();
    }
}
