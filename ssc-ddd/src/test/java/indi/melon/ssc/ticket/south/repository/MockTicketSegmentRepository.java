package indi.melon.ssc.ticket.south.repository;

import indi.melon.ssc.ticket.domain.south.repository.TicketSegmentRepository;
import indi.melon.ssc.ticket.domain.ticket.SegmentID;
import indi.melon.ssc.ticket.domain.ticket.TicketSegment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vvnn1
 * @since 2024/10/19 18:57
 */
public class MockTicketSegmentRepository implements TicketSegmentRepository {
    private final Map<SegmentID, TicketSegment<?>> db = new HashMap<>();

    @Override
    public TicketSegment<?> ticketSegmentOf(SegmentID id) {
        return db.get(id);
    }

    @Override
    public void save(TicketSegment<?> ticketBox) {
        db.put(ticketBox.getId(), ticketBox);
    }

    @Override
    public void delete(SegmentID id) {
        db.remove(id);
    }
}
