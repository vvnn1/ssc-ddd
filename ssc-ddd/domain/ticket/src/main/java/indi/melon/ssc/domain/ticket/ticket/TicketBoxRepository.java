package indi.melon.ssc.domain.ticket.ticket;

/**
 * @author vvnn1
 * @since 2024/4/9 13:44
 */
public interface TicketBoxRepository {
    TicketBox<?> get(BoxID id);
    void create(TicketBox<?> ticketBox);
    boolean update(TicketBox<?> ticketBox);
}
