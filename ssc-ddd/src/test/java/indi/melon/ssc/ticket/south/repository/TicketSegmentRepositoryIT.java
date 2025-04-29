package indi.melon.ssc.ticket.south.repository;

import indi.melon.ssc.ticket.domain.south.repository.TicketSegmentRepository;
import indi.melon.ssc.ticket.domain.ticket.*;
import indi.melon.ssc.ticket.south.repository.dao.TicketSegmentDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vvnn1
 * @since 2024/10/16 22:18
 */
@DataJpaTest(
        includeFilters = {
                 @ComponentScan.Filter(
                         type = FilterType.ASSIGNABLE_TYPE,
                         classes = {TicketSegmentRepository.class, TicketSegmentDao.class}
                 )
        }
)
public class TicketSegmentRepositoryIT {
    @Autowired
    private TicketSegmentRepository ticketSegmentRepository;

    @Test
    public void should_save_and_get_same_ticket_box() {

        AutoIncrTicketSegment box1 = buildAutoIncrTicketBox();
        ticketSegmentRepository.save(box1);

        AutoIncrTicketSegment box2 = (AutoIncrTicketSegment) ticketSegmentRepository.ticketSegmentOf(new SegmentID("0"));
        assertEquals(box1.getId(), box2.getId());
        assertEquals(box1.getCurrentMaxTicket(), box2.getCurrentMaxTicket());
        assertEquals(box1.getTicketNum(), box2.getTicketNum());
        assertEquals(box1.getDesc(), box2.getDesc());
        assertEquals(box1.getType(), box2.getType());

        UuidTicketSegment box3 = buildUuidTicketBox();
        ticketSegmentRepository.save(box3);

        UuidTicketSegment box4 = (UuidTicketSegment) ticketSegmentRepository.ticketSegmentOf(new SegmentID("1"));
        assertEquals(box3.getId(), box4.getId());
        assertEquals(box3.getTicketNum(), box4.getTicketNum());
        assertEquals(box3.getDesc(), box4.getDesc());
        assertEquals(box3.getType(), box4.getType());
    }

    @Test
    public void should_delete_existing_ticket_box() {
        assertNull(ticketSegmentRepository.ticketSegmentOf(new SegmentID("0")));

        AutoIncrTicketSegment box1 = buildAutoIncrTicketBox();
        ticketSegmentRepository.save(box1);

        assertNotNull(ticketSegmentRepository.ticketSegmentOf(new SegmentID("0")));
        ticketSegmentRepository.delete(new SegmentID("0"));

        assertNull(ticketSegmentRepository.ticketSegmentOf(new SegmentID("0")));
    }

    private AutoIncrTicketSegment buildAutoIncrTicketBox() {
        final String bizTag = "0";
        final Long currentMaxTicket = 0L;
        final Integer ticketNum = 10;
        final String desc = "测试id";

        return new AutoIncrTicketSegment(
                new SegmentID(bizTag),
                currentMaxTicket,
                ticketNum,
                desc
        );
    }

    private UuidTicketSegment buildUuidTicketBox() {
        final String bizTag = "1";
        final Integer ticketNum = 10;
        final String desc = "测试id_uuid";

        return new UuidTicketSegment(
                new SegmentID(bizTag),
                ticketNum,
                desc
        );
    }
}
