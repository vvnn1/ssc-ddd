package com.github.vvnn1.domain.repository;

import com.github.vvnn1.domain.entity.TicketBox;

/**
 * @author vvnn1
 * @since 2024/4/9 13:44
 */
public interface TicketBoxRepository {
    <T> TicketBox<T> genTicketBox(String module, Class<T> ticketClazz);
    <T> void createTicketBox(TicketBox<T> ticketBox);
}
