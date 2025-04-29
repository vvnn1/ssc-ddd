package indi.melon.ssc.ticket.domain.ticket;

import indi.melon.ssc.ticket.domain.thread.ParallelThread;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vvnn1
 * @since 2024/9/21 12:14
 */
class TicketBoxTest {

    @Test
    public void should_be_thread_safe_and_has_a_correct_sequence() throws InterruptedException {
        final long tickets_count = 10000;
        List<Long> tickets = new ArrayList<>();
        for (int i = 0; i < tickets_count; i++) {
            tickets.add((long) i);
        }

        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        CountDownLatch countDownLatch = new CountDownLatch(3);
        Set<Long> results = Collections.synchronizedSet(new TreeSet<>());
        TicketBox<Long> testTicketBox = new TicketBox<>(
                new TicketSegment<>(new SegmentID("test_ticket"), 1000, "test", TicketEnum.AUTO_INCREMENT) {
                    @Override
                    protected Collection<Long> genTickets(Integer ticketNums) {
                        return tickets;
                    }
                }
        );
        startGetTicket(cyclicBarrier, countDownLatch, testTicketBox, results);
        startGetTicket(cyclicBarrier, countDownLatch, testTicketBox, results);
        startGetTicket(cyclicBarrier, countDownLatch, testTicketBox, results);
        countDownLatch.await();

        assertEquals(tickets_count, results.size());

        assertTrue(results.containsAll(tickets));
    }

    private void startGetTicket(CyclicBarrier cyclicBarrier, CountDownLatch countDownLatch, TicketBox<Long> testTicketBox, Set<Long> results) {
        new ParallelThread(cyclicBarrier, countDownLatch, () -> {
            try{
                while (testTicketBox.hasNext()) {
                    results.add(testTicketBox.next());
                }
            }catch (NoSuchElementException ignore){

            }
        }).start();
    }

    @Test
    public void should_throw_exception_when_no_more_element(){
        TicketBox<Long> testTicketBox = new TicketBox<>(
                new TicketSegment<>(new SegmentID("test_ticket"), 1000, "test", TicketEnum.AUTO_INCREMENT) {
                    @Override
                    protected Collection<Long> genTickets(Integer ticketNums) {
                        return Arrays.asList(1L,2L,3L);
                    }
                }
        );


        while (testTicketBox.hasNext()) {
            testTicketBox.next();
        }

        assertThrows(NoSuchElementException.class, testTicketBox::next);
    }
}