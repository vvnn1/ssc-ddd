package indi.melon.ssc.domain.common.cqrs;

public interface DomainEventListener<T extends DomainEvent> {
    void onEvent(T event);
}
