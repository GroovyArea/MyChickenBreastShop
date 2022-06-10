package com.daniel.outbox.event;

import com.daniel.domain.VO.OutBox;
import com.daniel.mapper.OutBoxMapper;
import com.daniel.utility.ObjectMapperUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 아웃박스 객체 DB에 저장
 */
@Component
@RequiredArgsConstructor
public class OutBoxEventHandler {

    private static final String ORDER = "주문";
    private static final String EMAIL = "이메일";
    private static final String CART = "장바구니 주문";

    private final OutBoxMapper outBoxMapper;

    @EventListener
    public void doOutBoxEvent(OutBoxEvent outBoxEvent) {
        switch (outBoxEvent.getEventAction()) {
            case EMAIL:
                outBoxMapper.insertEmailOutBox(OutBox.builder()
                        .aggregateId(outBoxEvent.getAggregateId())
                        .aggregateType(outBoxEvent.getAggregateType())
                        .eventType(outBoxEvent.getEventType())
                        .payload(outBoxEvent.getPayload())
                        .build());
                break;
            case ORDER:
                outBoxMapper.insertOrderOutBox(OutBox.builder()
                        .aggregateId(outBoxEvent.getAggregateId())
                        .aggregateType(outBoxEvent.getAggregateType())
                        .eventType(outBoxEvent.getEventType())
                        .payload(outBoxEvent.getPayload())
                        .build());
                break;
            case CART:
                for (int i = 0; i < outBoxEvent.getCartList().size(); i++) {
                    JsonNode jsonNode = ObjectMapperUtil.getMapper().convertValue(outBoxEvent.getCartList().get(i), JsonNode.class);
                    outBoxMapper.insertOrderOutBox(OutBox.builder()
                            .aggregateId((long) outBoxEvent.getCartList().get(i).getItemNumber())
                            .aggregateType(outBoxEvent.getCartList().get(i).getClass().getSimpleName())
                            .eventType(outBoxEvent.getCartList().get(i).getClass().getSimpleName())
                            .payload(jsonNode.toString())
                            .build());
                }
                break;
        }
    }
}
