package indi.melon.ssc.domain.ticket.ticket;

import indi.melon.ssc.domain.ticket.ticket.BoxID;
import indi.melon.ssc.domain.ticket.ticket.TicketEnum;
import indi.melon.ssc.domain.ticket.ticket.UuidTicketBox;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vvnn1
 * @since 2024/9/21 12:01
 */
class UuidTicketBoxTest {

    @Test
    void should_gen_ticketNum_tickets() {
        UuidTicketBox uuidTicketBox = createUuidTicketBox();
        Collection<String> tickets = uuidTicketBox.genTickets(uuidTicketBox.getCurrentMaxTicket(), uuidTicketBox.getTicketNum());
        HashSet<String> set = new HashSet<>(tickets);
        assertEquals(set.size(), uuidTicketBox.getTicketNum());
    }

    @Test
    void should_not_change_and_its_useless() {
        UuidTicketBox uuidTicketBox = createUuidTicketBox();
        String incrMaxTicket = uuidTicketBox.incrMaxTicket(uuidTicketBox.getCurrentMaxTicket(), uuidTicketBox.getTicketNum());
        assertEquals(incrMaxTicket, uuidTicketBox.getCurrentMaxTicket());
    }

    private UuidTicketBox createUuidTicketBox(){
        UuidTicketBox uuidTicketBox = new UuidTicketBox();
        uuidTicketBox.setId(new BoxID("test_id"));
        uuidTicketBox.setCurrentMaxTicket(UUID.randomUUID().toString());
        uuidTicketBox.setTicketNum(5);
        uuidTicketBox.setType(TicketEnum.AUTO_INCREMENT);
        uuidTicketBox.setDesc("for test");
        return uuidTicketBox;
    }
}