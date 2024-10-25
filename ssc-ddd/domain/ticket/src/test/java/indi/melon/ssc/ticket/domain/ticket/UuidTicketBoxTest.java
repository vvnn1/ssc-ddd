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
        return new UuidTicketBox(
                new BoxID("test_id"),
                5,
                "for test"
        );
    }
}