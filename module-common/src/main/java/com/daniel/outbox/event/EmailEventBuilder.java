package com.daniel.outbox.event;

import com.daniel.domain.vo.EmailKey;
import com.daniel.utility.ObjectMapperUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 이벤트 빌더 <br>
 * 발생된 이벤트를 가공, payload를 json 형태로 변환 후 객체로 반환
 */
@Component
@Slf4j
public class EmailEventBuilder implements OutBoxEventBuilder<EmailKeyCreated> {

    private static final String EVENT_ACTION = "이메일";

    @Override
    public OutBoxEvent createOutBoxEvent(EmailKeyCreated domainEvent) {

        JsonNode jsonNode = ObjectMapperUtil.getMapper().convertValue(domainEvent, JsonNode.class);

        return new OutBoxEvent.OutBoxEventBuilder()
                .aggregateId(domainEvent.getEmailKeyId())
                .aggregateType(EmailKey.class.getSimpleName())
                .eventType(domainEvent.getClass().getSimpleName())
                .eventAction(EVENT_ACTION)
                .payload(jsonNode.toString())
                .build();

    }
}
