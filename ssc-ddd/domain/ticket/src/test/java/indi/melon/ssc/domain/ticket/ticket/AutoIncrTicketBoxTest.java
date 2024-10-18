package indi.melon.ssc.domain.ticket.ticket;

import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author vvnn1
 * @since 2024/9/21 11:21
 */
class AutoIncrTicketBoxTest {

    @Test
    void should_gen_ticketNums_tickets_and_incr_1_on_currentMaxTicket() {
        AutoIncrTicketBox autoIncrTicketBox = createAutoIncrTicketBox();
        Collection<Integer> tickets = autoIncrTicketBox.genTickets(
                autoIncrTicketBox.getCurrentMaxTicket(),
                autoIncrTicketBox.getTicketNum()
        );

        assertEquals(tickets.size(), (int) autoIncrTicketBox.getTicketNum());

        for (int i = 0; i < autoIncrTicketBox.getTicketNum(); i++) {
            assertTrue(tickets.contains(autoIncrTicketBox.getCurrentMaxTicket() + i + 1));
        }
    }

    @Test
    void should_return_new_maxTicket_that_equals_currentMaxTicket_add_ticketNum() {
        AutoIncrTicketBox autoIncrTicketBox = createAutoIncrTicketBox();
        Integer incrMaxTicket = autoIncrTicketBox.incrMaxTicket(
                autoIncrTicketBox.getCurrentMaxTicket(),
                autoIncrTicketBox.getTicketNum()
        );

        assertEquals(incrMaxTicket, autoIncrTicketBox.getCurrentMaxTicket() + autoIncrTicketBox.getTicketNum());
    }

    private AutoIncrTicketBox createAutoIncrTicketBox(){
        AutoIncrTicketBox autoIncrTicketBox = new AutoIncrTicketBox();
        autoIncrTicketBox.setId(new BoxID("test_id"));
        autoIncrTicketBox.setCurrentMaxTicket(0);
        autoIncrTicketBox.setTicketNum(5);
        autoIncrTicketBox.setType(TicketEnum.AUTO_INCREMENT);
        autoIncrTicketBox.setDesc("for test");
        return autoIncrTicketBox;
    }
}