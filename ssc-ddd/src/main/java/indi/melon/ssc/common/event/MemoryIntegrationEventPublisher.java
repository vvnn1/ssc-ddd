package indi.melon.ssc.common.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author wangmenglong
 * @since 2025/5/29 19:58
 */
@Component
public class MemoryIntegrationEventPublisher implements IntegrationEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public MemoryIntegrationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void publisher(Collection<IntegrationEvent> events) {
        for (IntegrationEvent event : events) {
            publisher(event);
        }
    }

    @Override
    public void publisher(IntegrationEvent event) {
        eventPublisher.publishEvent(event);
    }
}
