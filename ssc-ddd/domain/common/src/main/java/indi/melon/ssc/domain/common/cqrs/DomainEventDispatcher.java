package indi.melon.ssc.domain.common.cqrs;

import java.util.Collection;

public interface DomainEventDispatcher {
    void dispatchNow(Collection<DomainEvent> event);
}
