package indi.melon.ssc.ticket.south.repository.dao;

import indi.melon.ssc.ticket.domain.ticket.SegmentID;
import indi.melon.ssc.ticket.domain.ticket.TicketSegment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author vvnn1
 * @since 2024/10/16 21:55
 */
public interface TicketSegmentDao extends JpaRepository<TicketSegment<?>, SegmentID> {
}
