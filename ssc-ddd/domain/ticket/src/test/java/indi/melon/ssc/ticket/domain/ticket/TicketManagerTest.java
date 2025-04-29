package indi.melon.ssc.ticket.domain.ticket;

import indi.melon.ssc.ticket.domain.south.repository.TicketSegmentRepository;
import indi.melon.ssc.ticket.domain.thread.ParallelThread;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vvnn1
 * @since 2024/9/22 12:56
 */
class TicketManagerTest {
    private TicketManager ticketManager;
    private static final int cacheSize = 3;
    private static final long currentMaxTicket = 0;
    private static final int ticketNum = 10;

    @BeforeEach
    public void init(){
        TicketSegmentRepository repository = new TestTicketSegmentRepositoryImpl();
        ticketManager = new TicketManager(repository, cacheSize);
    }

    @Test
    void should_return_ticket_by_right_id_and_return_null_if_id_not_exist() {
        Object ticket_1 = ticketManager.require(new SegmentID("ticket_1"));
        assertNotNull(ticket_1);

        Object ticket_3 = ticketManager.require(new SegmentID("ticket_3"));
        assertNull(ticket_3);
    }

    @Test
    void should_get_uniq_ticket_when_parallel_thread() throws InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(4);
        CountDownLatch countDownLatch = new CountDownLatch(4);
        Set<Object> tickets = Collections.synchronizedSet(new HashSet<>());
        AtomicBoolean isUniq = new AtomicBoolean(true);

        multiThreadRequireTicket(cyclicBarrier, countDownLatch, tickets, isUniq);
        multiThreadRequireTicket(cyclicBarrier, countDownLatch, tickets, isUniq);
        multiThreadRequireTicket(cyclicBarrier, countDownLatch, tickets, isUniq);
        multiThreadRequireTicket(cyclicBarrier, countDownLatch, tickets, isUniq);
        multiThreadRequireTicket(cyclicBarrier, countDownLatch, tickets, isUniq);

        countDownLatch.await();
        assertTrue(isUniq.get());
    }

    private void multiThreadRequireTicket(CyclicBarrier cyclicBarrier, CountDownLatch countDownLatch, Set<Object> tickets, AtomicBoolean isUniq) {
        new ParallelThread(cyclicBarrier, countDownLatch, () -> {
            for (int i = 0; i < 1000; i++) {
                Object ticket = ticketManager.require(new SegmentID("ticket_1"));
                if (tickets.contains(ticket)) {
                    isUniq.set(false);
                    return;
                }
                tickets.add(ticket);
            }
        }).start();
    }

    static class TestTicketSegmentRepositoryImpl implements TicketSegmentRepository{
        private final Map<SegmentID, TicketSegment<?>> dbMap = new HashMap<>(){{
            put(
                    new SegmentID("ticket_1"),
                    createTicketBox(new SegmentID("ticket_1"))
            );
            put(
                    new SegmentID("ticket_2"),
                    createTicketBox(new SegmentID("ticket_2"))
            );
        }};


        @Override
        public TicketSegment<?> ticketSegmentOf(SegmentID id) {
            return dbMap.get(id);
        }

        @Override
        public void save(TicketSegment<?> ticketSegment) {
            dbMap.put(ticketSegment.getId(), ticketSegment);
        }

        @Override
        public void delete(SegmentID id) {
            dbMap.remove(id);
        }

        private AutoIncrTicketSegment createTicketBox(SegmentID id){
            return new AutoIncrTicketSegment(
                    id,
                    currentMaxTicket,
                    ticketNum,
                    "test desc"
            );
        }
    }
}