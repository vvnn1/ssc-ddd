package indi.melon.ssc.ticket.domain.ticket;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vvnn1
 * @since 2024/9/21 12:01
 */
class UuidTicketBoxTest {

    @Test
    void should_gen_ticketNum_tickets() {
        UuidTicketBox uuidTicketBox = createUuidTicketBox();
        Collection<String> tickets = uuidTicketBox.genTickets(uuidTicketBox.getTicketNum());
        HashSet<String> set = new HashSet<>(tickets);
        assertEquals(set.size(), uuidTicketBox.getTicketNum());
    }

    private UuidTicketBox createUuidTicketBox(){
        UuidTicketBox uuidTicketBox = new UuidTicketBox();
        uuidTicketBox.setId(new BoxID("test_id"));
        uuidTicketBox.setTicketNum(5);
        uuidTicketBox.setType(TicketEnum.AUTO_INCREMENT);
        uuidTicketBox.setDesc("for test");
        return uuidTicketBox;
    }
}