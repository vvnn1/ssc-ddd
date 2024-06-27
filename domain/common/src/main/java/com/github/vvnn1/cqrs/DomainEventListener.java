package com.github.vvnn1.cqrs;

public interface DomainEventListener<T extends DomainEvent> {
    void onEvent(T event);
}
