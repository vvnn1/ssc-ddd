package indi.melon.ssc.ticket.south.repository;

import indi.melon.ssc.ticket.domain.south.repository.TicketBoxRepository;
import indi.melon.ssc.ticket.domain.ticket.BoxID;
import indi.melon.ssc.ticket.domain.ticket.TicketBox;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vvnn1
 * @since 2024/10/19 18:57
 */
public class MockTicketBoxRepository implements TicketBoxRepository {
    private final Map<BoxID, TicketBox<?>> db = new HashMap<>();

    @Override
    public TicketBox<?> ticketBoxOf(BoxID id) {
        return db.get(id);
    }

    @Override
    public void save(TicketBox<?> ticketBox) {
        db.put(ticketBox.getId(), ticketBox);
    }

    @Override
    public void delete(BoxID id) {
        db.remove(id);
    }
}
