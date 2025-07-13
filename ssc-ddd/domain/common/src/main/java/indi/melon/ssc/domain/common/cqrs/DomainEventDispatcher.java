package indi.melon.ssc.domain.common.cqrs;

import java.util.Collection;

/**
 * 领域事件发布器
 */
public interface DomainEventDispatcher {
    void dispatchNow(Collection<DomainEvent> event);
}
