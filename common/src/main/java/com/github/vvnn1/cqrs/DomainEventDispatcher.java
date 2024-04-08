package com.github.vvnn1.cqrs;

import java.util.Collection;

public interface DomainEventDispatcher {
    void dispatchNow(Collection<DomainEvent> event);
}
