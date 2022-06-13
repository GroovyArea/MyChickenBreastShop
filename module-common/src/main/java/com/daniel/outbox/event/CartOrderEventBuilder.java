package com.daniel.outbox.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 장바구니 주문 아웃박스 이벤트 객체 빌더
 */
@Component
@Slf4j
public class CartOrderEventBuilder implements OutBoxEventBuilder<List<OrderCreated>> {

    private final static String EVENT_ACTION = "장바구니 주문";

    @Override
    public OutBoxEvent createOutBoxEvent(List<OrderCreated> domainEvent) {
        long firstId = domainEvent.get(0).getItemNumber();

        return new OutBoxEvent.OutBoxEventBuilder()
                .aggregateId(firstId)
                .aggregateType(List.class.getSimpleName())
                .eventType(domainEvent.getClass().getSimpleName())
                .eventAction(EVENT_ACTION)
                .cartList(domainEvent)
                .build();
    }
}
