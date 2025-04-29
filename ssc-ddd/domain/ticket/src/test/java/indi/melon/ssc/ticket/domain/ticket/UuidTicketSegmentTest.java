package indi.melon.ssc.ticket.domain.ticket;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vvnn1
 * @since 2024/9/21 12:01
 */
class UuidTicketSegmentTest {

    @Test
    void should_gen_ticketNum_tickets() {
        UuidTicketSegment uuidTicketSegment = createUuidTicketBox();
        Collection<String> tickets = uuidTicketSegment.genTickets();
        HashSet<String> set = new HashSet<>(tickets);
        assertEquals(set.size(), uuidTicketSegment.getTicketNum());
    }

    private UuidTicketSegment createUuidTicketBox(){
        return new UuidTicketSegment(
                new SegmentID("test_id"),
                5,
                "for test"
        );
    }
}