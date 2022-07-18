package com.daniel.service;

import com.daniel.domain.dto.user.UserEmailRequestDTO;
import com.daniel.domain.dto.user.UserJoinDTO;
import com.daniel.domain.vo.EmailKey;
import com.daniel.exceptions.error.EmailAuthException;
import com.daniel.mapper.EmailKeyMapper;
import com.daniel.outbox.event.EmailKeyCreated;
import com.daniel.outbox.event.OutBoxEventBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * mail 전송 서비스 <br>
 * 요청 이메일로 인증 번호를 전송하고 대조한다.
 *
 * <pre>
 *     <b>History</b>
 *     김남영, 1.0, 2022.05.26 최초 작성
 *     1.1, 2022.06.08 이메일 전송 기능 다른 모듈로 이동, 아웃 박스 패턴 적용
 * </pre>
 *
 * @author 김남영
 * @version 1.1
 */
@Service
@Slf4j
public class EmailService {

    private final Random random;

    private static final String INVALID_AUTH_KEY = "인증 번호가 일치하지 않습니다.";

    private final RedisService redisService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final OutBoxEventBuilder<EmailKeyCreated> outBoxEventBuilder;
    private final EmailKeyMapper emailKeyMapper;

    public EmailService(RedisService redisService, ApplicationEventPublisher applicationEventPublisher, OutBoxEventBuilder<EmailKeyCreated> outBoxEventBuilder, EmailKeyMapper emailKeyMapper) throws NoSuchAlgorithmException {
        this.redisService = redisService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.outBoxEventBuilder = outBoxEventBuilder;
        this.emailKeyMapper = emailKeyMapper;
        this.random = SecureRandom.getInstanceStrong();
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

    /**
     * 이메일을 받고 관련 정보를 DB에 저장 후 아웃박스 패턴을 위해 이벤트 발생
     *
     * @param emailRequestDTO 이메일 DTO
     */
    @Transactional
    public void saveEmailKey(UserEmailRequestDTO emailRequestDTO) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiredTime = now.plusMinutes(5);
        EmailKey emailKey = toEmailKeyVO(expiredTime, emailRequestDTO.getUserEmail());

        emailKeyMapper.insertEmailKey(emailKey);
        EmailKey saved = emailKeyMapper.selectEmailKey(emailKey.getId());

        applicationEventPublisher.publishEvent(
                outBoxEventBuilder.createOutBoxEvent(EmailKeyCreated.builder()
                        .emailKeyId(saved.getId())
                        .emailKey(saved.getEmailKey())
                        .email(saved.getEmail())
                        .build())
        );
    }

    private EmailKey toEmailKeyVO(LocalDateTime expiredAt, String email) {
        int randomKey = random.nextInt(888888) + 111111;
        return EmailKey.builder()
                .emailKey((long) randomKey)
                .email(email)
                .expiredAt(expiredAt)
                .build();
    }
}
