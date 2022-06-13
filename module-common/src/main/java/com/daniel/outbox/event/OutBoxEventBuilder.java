package com.daniel.outbox.event;

public interface OutBoxEventBuilder<T> {

    OutBoxEvent createOutBoxEvent(T domainEvent);
}
