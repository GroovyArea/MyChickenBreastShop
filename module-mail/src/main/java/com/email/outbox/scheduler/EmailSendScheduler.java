package com.email.outbox.scheduler;

import com.daniel.outbox.dto.MailDTO;
import com.email.domain.VO.OutBox;
import com.email.exception.error.FailedPayloadConvertException;
import com.email.mapper.OutBoxEmailMapper;
import com.email.outbox.message.MailContentService;
import com.email.outbox.message.SendMailService;
import com.email.service.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * 이메일 스케쥴러 <br>
 * 10초 간격으로 outbox 테이블 데이터 조회 후 메일 발송
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EmailSendScheduler {

    private static final long EXPIRE_DURATION = 60 * 5L;

    private final RedisService redisService;
    private final OutBoxEmailMapper outBoxMapper;
    private final SendMailService sendMailService;
    private final MailContentService mailContentService;

    @Scheduled(cron = "0/10 * * * * ?")
    public void schedulingValidNumberEmail() {
        ObjectMapper objectMapper = new ObjectMapper();

        log.info("이메일 전송 중...");

        List<OutBox> outBoxList = outBoxMapper.selectAllEmailOutBox();
        if (!outBoxList.isEmpty()) {
            List<Long> completedList = new LinkedList<>();
            outBoxList.forEach(outBox -> {
                validateEmailNumber(objectMapper, completedList, outBox);
            });
            if (!completedList.isEmpty()) {
                outBoxMapper.deleteAllById(completedList);
            }
        }
    }

    @Transactional
    void validateEmailNumber(ObjectMapper objectMapper, List<Long> completedList, OutBox outBox) {
        String payload = outBox.getPayload();
        try {
            JsonNode jsonNode = objectMapper.readTree(payload);
            String userEmail = jsonNode.get("email").asText();
            String authKey = jsonNode.get("email_key").asText();

            MailDTO content = mailContentService.createMailContent(payload);

            sendMailService.sendEmail(content);

            completedList.add(outBox.getId());

            redisService.setDataExpire(userEmail, authKey, EXPIRE_DURATION);
        } catch (MailException e) {
            log.error("메일 발송 중 오류 발생 . . .");
            outBoxMapper.insertOutBox(outBox);
        } catch (FailedPayloadConvertException | JsonProcessingException e) {
            log.error(e.getMessage());
            outBoxMapper.insertOutBox(outBox);
        }
    }


}
