package indi.melon.ssc.ticket.north.local.appservice;

import indi.melon.ssc.SscBaseTest;
import indi.melon.ssc.common.exception.ApplicationValidationException;
import indi.melon.ssc.ticket.domain.south.repository.TicketSegmentRepository;
import indi.melon.ssc.ticket.domain.ticket.AutoIncrTicketSegment;
import indi.melon.ssc.ticket.domain.ticket.SegmentID;
import indi.melon.ssc.ticket.domain.ticket.UuidTicketSegment;
import indi.melon.ssc.ticket.north.local.message.TicketCreateCommand;
import indi.melon.ssc.ticket.south.repository.MockTicketSegmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author wangmenglong
 * @since 2024/10/17 19:46
 */
@Import(TicketAppServiceIT.MockConfiguration.class)
class TicketAppServiceIT extends SscBaseTest {
    @Autowired
    private TicketAppService ticketAppService;
    @Autowired
    private TicketSegmentRepository ticketSegmentRepository;

    @BeforeEach
    void setUp() {
        AutoIncrTicketSegment autoIncrTicketSegment = new AutoIncrTicketSegment(
                new SegmentID("record"),
                0L,
                10,
                "auto incr box"
        );

        ticketSegmentRepository.save(autoIncrTicketSegment);

        UuidTicketSegment uuidTicketSegment = new UuidTicketSegment(
                new SegmentID("treeNode"),
                10,
                "uuid box"
        );
        ticketSegmentRepository.save(uuidTicketSegment);
    }


    @Test
    public void should_get_right_ticket() {
        assertThrows(ApplicationValidationException.class, () -> ticketAppService.require(
                new TicketCreateCommand<>(null, Long.class)
        ));

        assertThrows(ApplicationValidationException.class, () -> ticketAppService.require(
                new TicketCreateCommand<>("record", null)
        ));

        Long recordId = ticketAppService.require(
                new TicketCreateCommand<>("record", Long.class)
        );

        assertEquals(1L, recordId);

        String treeNodeId = ticketAppService.require(
                new TicketCreateCommand<>("treeNode", String.class)
        );

        assertNotNull(treeNodeId);
        assertEquals(36, treeNodeId.length());
    }

    @TestConfiguration
    static class MockConfiguration {
        @Bean
        public TicketSegmentRepository ticketSegmentRepository() {
            return new MockTicketSegmentRepository();
        }
    }
}