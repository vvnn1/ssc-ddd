package com.github.vvnn1.domain.repository;

import com.github.vvnn1.domain.entity.TicketBox;
import com.github.vvnn1.domain.pojo.BoxID;

/**
 * @author vvnn1
 * @since 2024/4/9 13:44
 */
public interface TicketBoxRepository {
    <T> TicketBox<T> get(BoxID id);
    <T> void create(TicketBox<T> ticketBox);
    <T> void expiry(TicketBox<T> ticketBox);
}
