package indi.melon.ssc.domain.common.cqrs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractAggregateRoot {
    private transient final List<DomainEvent> domainEvents = new ArrayList<>();

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    public Collection<DomainEvent> domainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    protected void addEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }
}
