package indi.melon.ssc.domain.common.cqrs;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author vvnn1
 * @since 2024/10/3 14:47
 */
public abstract class AbstractDomainEvent implements DomainEvent{
    protected final String eventId;
    protected final String occurredOn;

    public AbstractDomainEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = LocalDateTime.now().toString();
    }
}
