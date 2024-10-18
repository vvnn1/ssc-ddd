package indi.melon.ssc.domain.ticket.ticket;

import indi.melon.ssc.domain.ticket.thread.ParallelThread;
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
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        CountDownLatch countDownLatch = new CountDownLatch(3);
        Set<Integer> results = Collections.synchronizedSet(new TreeSet<>());
        TestTicketBox testTicketBox = new TestTicketBox();
        startGetTicket(cyclicBarrier, countDownLatch, testTicketBox, results);
        startGetTicket(cyclicBarrier, countDownLatch, testTicketBox, results);
        startGetTicket(cyclicBarrier, countDownLatch, testTicketBox, results);
        countDownLatch.await();
        List<Integer> tickets = testTicketBox.tickets;

        assertEquals(tickets.size(), results.size());

        assertArrayEquals(tickets.toArray(), results.toArray(), () -> "expected: " + tickets + ". but actual: " + results);
    }

    private void startGetTicket(CyclicBarrier cyclicBarrier, CountDownLatch countDownLatch, TestTicketBox testTicketBox, Set<Integer> results) {
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
    public void if_no_more_element_throw_exception(){
        TestTicketBox testTicketBox = new TestTicketBox();
        for (int i = 0; i < testTicketBox.tickets.size(); i++) {
            testTicketBox.next();
        }

        assertFalse(testTicketBox.hasNext());
        assertThrows(NoSuchElementException.class, testTicketBox::next);
    }

    static class TestTicketBox extends TicketBox<Integer> {

        private final List<Integer> tickets = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);

        @Override
        protected Collection<Integer> genTickets(Integer currentMaxTicket, Integer ticketNums) {
            return tickets;
        }

        @Override
        protected Integer incrMaxTicket(Integer currentMaxTicket, Integer ticketNums) {
            return 233;
        }
    }
}