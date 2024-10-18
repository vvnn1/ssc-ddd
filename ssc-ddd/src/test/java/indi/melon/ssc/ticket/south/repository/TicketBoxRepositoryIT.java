package indi.melon.ssc.ticket.south.repository;

import indi.melon.ssc.SscTestApplication;
import indi.melon.ssc.ticket.domain.south.repository.TicketBoxRepository;
import indi.melon.ssc.ticket.domain.ticket.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vvnn1
 * @since 2024/10/16 22:18
 */
@SpringBootTest(classes = SscTestApplication.class)
public class TicketBoxRepositoryIT {
    @Autowired
    private TicketBoxRepository ticketBoxRepository;

    @Test
    public void should_save_and_get_same_ticket_box() {

        AutoIncrTicketBox box1 = buildAutoIncrTicketBox();
        ticketBoxRepository.save(box1);

        AutoIncrTicketBox box2 = (AutoIncrTicketBox) ticketBoxRepository.ticketBoxOf(new BoxID("0"));
        assertEquals(box1.getId(), box2.getId());
        assertEquals(box1.getCurrentMaxTicket(), box2.getCurrentMaxTicket());
        assertEquals(box1.getTicketNum(), box2.getTicketNum());
        assertEquals(box1.getDesc(), box2.getDesc());
        assertEquals(box1.getType(), box2.getType());

        UuidTicketBox box3 = buildUuidTicketBox();
        ticketBoxRepository.save(box3);

        UuidTicketBox box4 = (UuidTicketBox) ticketBoxRepository.ticketBoxOf(new BoxID("1"));
        assertEquals(box3.getId(), box4.getId());
        assertEquals(box3.getTicketNum(), box4.getTicketNum());
        assertEquals(box3.getDesc(), box4.getDesc());
        assertEquals(box3.getType(), box4.getType());
    }

    @Test
    public void should_remove_existing_ticket_box() {
        assertNull(ticketBoxRepository.ticketBoxOf(new BoxID("0")));

        AutoIncrTicketBox box1 = buildAutoIncrTicketBox();
        ticketBoxRepository.save(box1);

        assertNotNull(ticketBoxRepository.ticketBoxOf(new BoxID("0")));
        ticketBoxRepository.remove(new BoxID("0"));

        assertNull(ticketBoxRepository.ticketBoxOf(new BoxID("0")));
    }

    private AutoIncrTicketBox buildAutoIncrTicketBox() {
        final String bizTag = "0";
        final Long currentMaxTicket = 0L;
        final Integer ticketNum = 10;
        final String desc = "测试id";

        return new AutoIncrTicketBox(
                new BoxID(bizTag),
                currentMaxTicket,
                ticketNum,
                desc
        );
    }

    private UuidTicketBox buildUuidTicketBox() {
        final String bizTag = "1";
        final Integer ticketNum = 10;
        final String desc = "测试id_uuid";

        return new UuidTicketBox(
                new BoxID(bizTag),
                ticketNum,
                desc
        );
    }
}
