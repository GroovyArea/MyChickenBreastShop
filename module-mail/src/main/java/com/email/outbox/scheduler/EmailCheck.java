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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailCheck {

    private static final long EXPIRE_DURATION = 60 * 5L;

    private final RedisService redisService;
    private final OutBoxEmailMapper outBoxMapper;
    private final SendMailService sendMailService;
    private final MailContentService mailContentService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
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
        } catch (MailException | FailedPayloadConvertException | JsonProcessingException e) {
            log.error(e.getMessage());
            outBoxMapper.deleteOutBox(outBox);
            outBoxMapper.insertOutBox(outBox);
        }
    }
}
