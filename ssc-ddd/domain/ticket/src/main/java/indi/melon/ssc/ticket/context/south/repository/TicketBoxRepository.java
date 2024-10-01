package indi.melon.ssc.ticket.context.south.repository;

import indi.melon.ssc.ticket.context.domain.ticket.BoxID;
import indi.melon.ssc.ticket.context.domain.ticket.TicketBox;

/**
 * @author vvnn1
 * @since 2024/4/9 13:44
 */
public interface TicketBoxRepository {
    TicketBox<?> get(BoxID id);
    void create(TicketBox<?> ticketBox);
    boolean update(TicketBox<?> ticketBox);
}
