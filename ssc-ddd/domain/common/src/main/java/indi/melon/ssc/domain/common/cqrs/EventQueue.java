package indi.melon.ssc.domain.common.cqrs;

import java.util.List;

public interface EventQueue {
    void enqueue(DomainEvent event);
    List<DomainEvent> queue();
}
