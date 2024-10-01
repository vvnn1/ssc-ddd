package indi.melon.ssc.context.ticket.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vvnn1
 * @since 2024/9/20 19:59
 */
class CyclicQueueTest {
    @Test
    void should_not_offer_when_queue_is_full() {
        CyclicQueue<Integer> queue = new CyclicQueue<>(2);
        assertTrue(queue.offer(1));
        assertTrue(queue.offer(1));
        assertFalse(queue.offer(1));
        queue.poll();
        assertTrue(queue.offer(1));
        assertFalse(queue.offer(1));
    }
}