package com.email.outbox.message;

import com.daniel.outbox.dto.MailDTO;
import com.email.exception.error.FailedPayloadConvertException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 이메일 내용 생성
 */
@Service
@RequiredArgsConstructor
public class MailContentService {

    @Value("${email.from}")
    private String FROM;
    @Value("${email.subject}")
    private String SUBJECT;
    @Value("${email.text}")
    private String TEXT;

    public MailDTO createMailContent(String payload) throws FailedPayloadConvertException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;

        try {
            jsonNode = objectMapper.readTree(payload);
        } catch (JsonProcessingException e) {
            throw new FailedPayloadConvertException();
        }

        String userEmail = jsonNode.get("email").asText();
        String authKey = jsonNode.get("email_key").asText();

        String content = TEXT + authKey;

        return toMailContentDTO(userEmail, FROM, SUBJECT, content);
    }

    private MailDTO toMailContentDTO(String to, String from, String title, String content) {
        MailDTO mailDTO = new MailDTO();
        mailDTO.setTo(to);
        mailDTO.setFrom(from);
        mailDTO.setTitle(title);
        mailDTO.setContent(content);
        return mailDTO;
    }
}
