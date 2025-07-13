package indi.melon.ssc.common.event;


import java.util.Collection;

/**
 * @author wangmenglong
 * @since 2025/5/29 11:00
 */
public interface IntegrationEventPublisher {
    void publisher(Collection<IntegrationEvent> events);
    void publisher(IntegrationEvent event);
}
