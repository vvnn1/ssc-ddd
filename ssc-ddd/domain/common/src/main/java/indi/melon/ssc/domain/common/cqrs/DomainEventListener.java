package indi.melon.ssc.domain.common.cqrs;

/**
 * 领域事件监听
 */
public interface DomainEventListener<T extends DomainEvent> {
    void onEvent(T event);
}
