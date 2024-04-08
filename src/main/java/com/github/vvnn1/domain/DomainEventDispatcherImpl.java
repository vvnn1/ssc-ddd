package com.github.vvnn1.domain;

import com.github.vvnn1.cqrs.DomainEvent;
import com.github.vvnn1.cqrs.DomainEventDispatcher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author vvnn1
 * @since 2024/4/6 15:00
 */
@Component
public class DomainEventDispatcherImpl implements DomainEventDispatcher {
    private final ApplicationEventPublisher publisher;

    public DomainEventDispatcherImpl(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void dispatchNow(Collection<DomainEvent> event) {
        for (DomainEvent domainEvent : event) {
            publisher.publishEvent(domainEvent);
        }
    }
}
