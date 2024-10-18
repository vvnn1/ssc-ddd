package indi.melon.ssc.ticket.domain.ticket;

import indi.melon.ssc.ticket.domain.south.repository.TicketBoxRepository;
import indi.melon.ssc.ticket.domain.thread.ParallelThread;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
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
    private static final int currentMaxTicket = 0;
    private static final int ticketNum = 10;

    @BeforeEach
    public void init(){
        TicketBoxRepository repository = new TestTicketBoxRepositoryImpl();
        BoxManager boxManager = new BoxManager(repository, cacheSize);
        ticketManager = new TicketManager(boxManager);
    }

    @Test
    void should_return_ticket_by_right_id_and_return_null_if_id_not_exist() {
        Object ticket_1 = ticketManager.require(new BoxID("ticket_1"));
        assertNotNull(ticket_1);

        Object ticket_3 = ticketManager.require(new BoxID("ticket_3"));
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
                Object ticket = ticketManager.require(new BoxID("ticket_1"));
                if (tickets.contains(ticket)) {
                    isUniq.set(false);
                    return;
                }
                tickets.add(ticket);
            }
        }).start();
    }

    static class TestTicketBoxRepositoryImpl implements TicketBoxRepository{
        private final Map<BoxID, AutoIncrTicketBox> dbMap = new HashMap<>(){{
            put(
                    new BoxID("ticket_1"),
                    createTicketBox(new BoxID("ticket_1"))
            );
            put(
                    new BoxID("ticket_2"),
                    createTicketBox(new BoxID("ticket_2"))
            );
        }};


        @Override
        public TicketBox<?> ticketBoxOf(BoxID id) {
            TicketBox<Long> ticketBox = dbMap.get(id);
            if (ticketBox == null){
                return null;
            }
            TicketBox<Long> testTicketBox = new AutoIncrTicketBox();
            copy(ticketBox, testTicketBox);
            return testTicketBox;
        }

        @Override
        public void save(TicketBox<?> ticketBox) {
            TicketBox autoIncrTicketBox = dbMap.get(ticketBox.getId());
            copy(ticketBox, autoIncrTicketBox);
        }

        @Override
        public void remove(BoxID id) {

        }

        private <T> void copy(TicketBox<T> box1, TicketBox<T> box2){
            box2.setId(box1.getId());
            box2.setTicketNum(box1.getTicketNum());
            box2.setUpdateTime(box1.getUpdateTime());
            box2.setDesc(box1.getDesc());
            box2.setType(box1.getType());
            if (box2 instanceof AutoIncrTicketBox b2 && box1 instanceof AutoIncrTicketBox b1){
                b2.setCurrentMaxTicket(b1.getCurrentMaxTicket());
            }
        }

        private AutoIncrTicketBox createTicketBox(BoxID id){
            AutoIncrTicketBox autoIncrTicketBox = new AutoIncrTicketBox();
            autoIncrTicketBox.setId(id);
            autoIncrTicketBox.setTicketNum(ticketNum);
            autoIncrTicketBox.setCurrentMaxTicket(currentMaxTicket);
            autoIncrTicketBox.setDesc("test desc");
            autoIncrTicketBox.setType(TicketEnum.AUTO_INCREMENT);
            autoIncrTicketBox.setUpdateTime(LocalDateTime.now());
            return autoIncrTicketBox;
        }
    }
}