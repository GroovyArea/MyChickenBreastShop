package com.daniel.controller.user;

import com.daniel.domain.dto.user.UserEmailRequestDTO;
import com.daniel.domain.dto.user.UserJoinDTO;
import com.daniel.exceptions.error.EmailAuthException;
import com.daniel.exceptions.error.UserExistsException;
import com.daniel.exceptions.error.UserNotExistsException;
import com.daniel.response.Message;
import com.daniel.service.EmailService;
import com.daniel.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/join")
public class JoinController {

    private static final String JOIN_MESSAGE = "Join succeed";
    private static final String EMAIL_MESSAGE = "Email auth key sent";

    private final UserService userService;
    private final EmailService emailService;

    /**
     * 회원 가입
     *
     * @param joinUser 회원가입 dto
     * @return 응답 객체
     * @throws UserExistsException      해당 아이디를 가진 유저 존재 시 예외 발생
     * @throws NoSuchAlgorithmException 암호화 알고리즘 잘못 사용시 예외 발생
     * @throws EmailAuthException       이메일 인증번호 불일치 시 예외
     * @throws UserNotExistsException   회원이 존재하지 않을 때 예외
     */
    @PostMapping
    public ResponseEntity<Message> joinAction(@RequestBody UserJoinDTO joinUser) throws UserExistsException, NoSuchAlgorithmException, EmailAuthException, UserNotExistsException {
        emailService.authEmail(joinUser);
        userService.addUser(joinUser);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                Message.builder()
                        .data(userService.findById(joinUser.getUserId()))
                        .message(JOIN_MESSAGE)
                        .build()
        );
    }

    /**
     * 이메일 인증 번호 전송 처리
     *
     * @param emailRequestDTO 이메일
     * @return 상태코드 200
     */
    @PostMapping("/email")
    public ResponseEntity<String> authEmailSendAction(@RequestBody UserEmailRequestDTO emailRequestDTO) {
        emailService.saveEmailKey(emailRequestDTO);
        return ResponseEntity.ok().body(EMAIL_MESSAGE);
    }
}
