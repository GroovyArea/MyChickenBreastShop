package com.daniel.outbox.event;

import com.daniel.domain.VO.EmailKey;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

/**
 * 이벤트 빌더 <br>
 * 발생된 이벤트를 가공, payload를 json 형태로 변환 후 객체로 반환
 */
@Component
@Slf4j
public class EmailEventBuilder implements OutBoxEventBuilder<EmailKeyCreated> {

    @Override
    public OutBoxEvent createOutBoxEvent(EmailKeyCreated domainEvent) {

        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(new LocalDateTimeSerializer(DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")));
        mapper.registerModule(javaTimeModule);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        JsonNode jsonNode = mapper.convertValue(domainEvent, JsonNode.class);

        return new OutBoxEvent(
                domainEvent.getEmailKeyId(),
                EmailKey.class.getSimpleName(),
                domainEvent.getClass().getSimpleName(),
                jsonNode.toString()
        );
    }
}
