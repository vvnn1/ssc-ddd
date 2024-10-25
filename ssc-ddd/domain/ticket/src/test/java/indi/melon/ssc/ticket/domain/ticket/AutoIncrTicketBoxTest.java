package indi.melon.ssc.ticket.domain.ticket;

import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author vvnn1
 * @since 2024/9/21 11:21
 */
class AutoIncrTicketBoxTest {
    private final Long currentMaxTicket = 0L;
    private final Integer ticketNum = 5;

    @Test
    void should_gen_ticketNums_tickets_and_incr_1_on_currentMaxTicket() {
        AutoIncrTicketBox autoIncrTicketBox = createAutoIncrTicketBox();
        Collection<Long> tickets = autoIncrTicketBox.genTickets(
                autoIncrTicketBox.getTicketNum()
        );

        assertEquals(tickets.size(), (int) autoIncrTicketBox.getTicketNum());

        for (int i = 0; i < autoIncrTicketBox.getTicketNum(); i++) {
            assertTrue(tickets.contains(currentMaxTicket + i + 1));
        }
    }

    @Test
    void should_incr_currentMaxTicket_after_gen_tickets() {
        AutoIncrTicketBox autoIncrTicketBox = createAutoIncrTicketBox();
        autoIncrTicketBox.genTickets(
                autoIncrTicketBox.getTicketNum()
        );

        assertEquals(currentMaxTicket + ticketNum, autoIncrTicketBox.getCurrentMaxTicket());
    }


    private AutoIncrTicketBox createAutoIncrTicketBox(){
        return new AutoIncrTicketBox(
                new BoxID("test_id"),
                currentMaxTicket,
                ticketNum,
                "for test"
        );
    }
}