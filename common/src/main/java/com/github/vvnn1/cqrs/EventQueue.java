package com.github.vvnn1.cqrs;

import java.util.List;

public interface EventQueue {
    void enqueue(DomainEvent event);
    List<DomainEvent> queue();
}
