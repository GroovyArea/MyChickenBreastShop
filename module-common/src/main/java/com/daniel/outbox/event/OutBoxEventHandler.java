package com.daniel.outbox.event;

import com.daniel.domain.VO.OutBox;
import com.daniel.mapper.OutBoxMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 아웃박스 객체 DB에 저장
 */
@Component
@RequiredArgsConstructor
public class OutBoxEventHandler {

    private final OutBoxMapper outBoxMapper;

    @EventListener
    public void doOutBoxEvent(OutBoxEvent outBoxEvent) {
        outBoxMapper.insertOutBox(OutBox.builder()
                .aggregateId(outBoxEvent.getAggregateId())
                .aggregateType(outBoxEvent.getAggregateType())
                .eventType(outBoxEvent.getEventType())
                .payload(outBoxEvent.getPayload())
                .build());
    }
}
