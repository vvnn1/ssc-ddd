package indi.melon.ssc.ticket.domain.ticket;

import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author vvnn1
 * @since 2024/9/21 11:21
 */
class AutoIncrTicketSegmentTest {
    private final Long currentMaxTicket = 0L;
    private final Integer ticketNum = 5;

    @Test
    void should_gen_ticketNums_tickets_and_incr_1_on_currentMaxTicket() {
        AutoIncrTicketSegment autoIncrTicketSegment = createAutoIncrTicketBox();
        Collection<Long> tickets = autoIncrTicketSegment.genTickets();

        assertEquals(tickets.size(), (int) autoIncrTicketSegment.getTicketNum());

        for (int i = 0; i < autoIncrTicketSegment.getTicketNum(); i++) {
            assertTrue(tickets.contains(currentMaxTicket + i + 1));
        }
    }

    @Test
    void should_incr_currentMaxTicket_after_gen_tickets() {
        AutoIncrTicketSegment autoIncrTicketSegment = createAutoIncrTicketBox();
        autoIncrTicketSegment.genTickets();

        assertEquals(currentMaxTicket + ticketNum, autoIncrTicketSegment.getCurrentMaxTicket());
    }


    private AutoIncrTicketSegment createAutoIncrTicketBox(){
        return new AutoIncrTicketSegment(
                new SegmentID("test_id"),
                currentMaxTicket,
                ticketNum,
                "for test"
        );
    }
}