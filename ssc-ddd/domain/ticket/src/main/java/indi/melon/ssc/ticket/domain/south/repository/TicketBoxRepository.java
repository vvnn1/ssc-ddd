package indi.melon.ssc.ticket.domain.south.repository;

import indi.melon.ssc.ticket.domain.ticket.BoxID;
import indi.melon.ssc.ticket.domain.ticket.TicketBox;

/**
 * @author vvnn1
 * @since 2024/4/9 13:44
 */
public interface TicketBoxRepository {
    TicketBox<?> ticketBoxOf(BoxID id);
    void save(TicketBox<?> ticketBox);
    void remove(BoxID id);
}
