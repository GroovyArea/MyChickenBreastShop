package me.daniel.service;

import me.daniel.domain.DTO.user.UserEmailRequestDTO;
import me.daniel.domain.DTO.user.UserJoinDTO;
import me.daniel.exceptions.EmailAuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

/**
 * mail 전송 서비스 <br>
 * 요청 이메일로 인증 번호를 전송하고 대조한다.
 *
 * <pre>
 *     <b>History</b>
 *     김남영, 1.0, 2022.05.26 최초 작성
 * </pre>
 *
 * @author 김남영
 * @version 1.0
 */
@Service
public class EmailService {

    private static final int RANDOM_KEY = new Random().nextInt(888888) + 111111;
    private static final String FROM = "095201a@naver.com";
    private static final String ENCODE = "utf-8";
    private static final String SUBJECT = "MyChickenShop 회원 가입 인증 번호 입니다.";
    private static final String TEXT = "회원 가입을 위한 인증 번호입니다 : <br>";
    private static final long EXPIRE_DURATION = 60 * 5L;
    private static final String INVALID_AUTH_KEY = "인증 번호가 일치하지 않습니다.";

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender javaMailSender;
    private final RedisService redisService;

    public EmailService(JavaMailSender javaMailSender, RedisService redisService) {
        this.javaMailSender = javaMailSender;
        this.redisService = redisService;
    }

    /**
     * 이메일로 인증 번호 전송 <br>
     * Redis 에 인증 번호 저장
     *
     * @param emailRequestDTO 이메일 정보
     */
    @Async("email-async-")
    public void sendEmail(UserEmailRequestDTO emailRequestDTO) {
        String authKey = String.valueOf(RANDOM_KEY);
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, ENCODE);
            helper.setFrom(FROM);
            helper.setTo(emailRequestDTO.getUserEmail());
            helper.setSubject(SUBJECT);
            helper.setText(TEXT + authKey, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }

        redisService.setDataExpire(emailRequestDTO.getUserEmail(), authKey, EXPIRE_DURATION);
    }

    /**
     * 인증 번호 검증
     *
     * @param joinUser 회원 가입 유저
     * @throws EmailAuthException 인증 번호 불일 치 시 예외 발생
     */
    public void authEmail(UserJoinDTO joinUser) throws EmailAuthException {
        String redisValue = redisService.getData(joinUser.getUserEmail());

        if (!joinUser.getAuthKey().equals(redisValue)) {
            throw new EmailAuthException(INVALID_AUTH_KEY);
        }
    }
}
