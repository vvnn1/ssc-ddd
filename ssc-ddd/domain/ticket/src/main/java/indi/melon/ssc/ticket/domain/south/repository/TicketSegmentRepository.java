package indi.melon.ssc.ticket.domain.south.repository;

import indi.melon.ssc.ticket.domain.ticket.SegmentID;
import indi.melon.ssc.ticket.domain.ticket.TicketSegment;

/**
 * @author vvnn1
 * @since 2024/4/9 13:44
 */
public interface TicketSegmentRepository {
    TicketSegment<?> ticketSegmentOf(SegmentID id);
    void save(TicketSegment<?> ticketBox);
    void delete(SegmentID id);
}
