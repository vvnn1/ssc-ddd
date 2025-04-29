package indi.melon.ssc.ticket.domain.ticket;

import indi.melon.ssc.ticket.domain.south.repository.TicketSegmentRepository;
import indi.melon.ssc.ticket.domain.thread.ParallelThread;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vvnn1
 * @since 2024/9/21 13:43
 */
class BoxManagerTest {

    private BoxManager boxManager;
    private TicketSegmentRepository repository;
    private static final int cacheSize = 3;
    private static final long currentMaxTicket = 0L;
    private static final int ticketNum = 10;

    @BeforeEach
    public void init() {
        repository = new MockTicketSegmentRepositoryImpl();
        boxManager = new BoxManager(repository, cacheSize);
    }

    @Test
    public void should_get_right_box_then_cache_some_box_and_update_currentMaxTicket() {
        assertNull(boxManager.get(
                new SegmentID("ticket_3")
        ));

        SegmentID segmentId = new SegmentID("ticket_1");
        TicketBox<?> ticketBox = boxManager.get(
                segmentId
        );

        assertEquals(segmentId, ticketBox.getSegmentId());
        Map<SegmentID, Queue<TicketBox<?>>> map = getTicketBoxQueue();
        Queue<TicketBox<?>> boxQueue = map.get(segmentId);
        assertEquals(boxQueue.size(), cacheSize);

        AutoIncrTicketSegment dbBox = (AutoIncrTicketSegment) repository.ticketSegmentOf(segmentId);
        assertEquals(dbBox.getCurrentMaxTicket(), currentMaxTicket + cacheSize * ticketNum);
    }

    @Test
    public void should_release_right_box_then_re_full_cache_and_update_currentMaxTicket() {
        long startId = 0L;
        SegmentID segmentId = new SegmentID("ticket_1");
        TicketBox<?> ticketBox = boxManager.get(
                segmentId
        );

        Map<SegmentID, Queue<TicketBox<?>>> map = getTicketBoxQueue();
        Queue<TicketBox<?>> boxQueue = map.get(segmentId);
        assertEquals(boxQueue.peek(), ticketBox);


        while (ticketBox.hasNext()) {
            Long id = (Long) ticketBox.next();
            assertSame(id, ++startId);
        }

        boxManager.release(ticketBox);
        assertNotEquals(boxQueue.peek(), ticketBox);
        assertEquals(boxQueue.size(), cacheSize);

        AutoIncrTicketSegment dbBox = (AutoIncrTicketSegment) repository.ticketSegmentOf(segmentId);
        assertEquals(dbBox.getCurrentMaxTicket(), currentMaxTicket + (cacheSize + 1) * ticketNum);

        TicketBox<?> peekBox = boxQueue.peek();
        boxManager.release(ticketBox);
        assertEquals(boxQueue.peek(), peekBox);

        AutoIncrTicketSegment dbBox2 = (AutoIncrTicketSegment) repository.ticketSegmentOf(segmentId);
        assertEquals(dbBox2.getCurrentMaxTicket(), currentMaxTicket + (cacheSize + 1) * ticketNum);

        TicketBox<?> ticketBox2 = boxManager.get(
                segmentId
        );
        while (ticketBox2.hasNext()) {
            Long id = (Long) ticketBox2.next();
            assertSame(id, ++startId);
        }
        boxManager.release(ticketBox2);
        AutoIncrTicketSegment dbBox3 = (AutoIncrTicketSegment) repository.ticketSegmentOf(segmentId);
        assertEquals(dbBox3.getCurrentMaxTicket(), currentMaxTicket + (cacheSize + 2) * ticketNum);
    }

    @Test
    public void should_get_same_box_although_multi_thread() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(4);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(4);

        SegmentID segmentId = new SegmentID("ticket_1");
        final TicketBox<?>[] boxs = new TicketBox<?>[4];
        startGetBoxThread(cyclicBarrier, countDownLatch, segmentId, boxs, 0);
        startGetBoxThread(cyclicBarrier, countDownLatch, segmentId, boxs, 1);
        startGetBoxThread(cyclicBarrier, countDownLatch, segmentId, boxs, 2);
        startGetBoxThread(cyclicBarrier, countDownLatch, segmentId, boxs, 3);

        countDownLatch.await();

        assertSame(boxs[0], boxs[1]);
        assertSame(boxs[1], boxs[2]);
        assertSame(boxs[2], boxs[3]);
    }

    @Test
    public void should_enable_cache_when_repository_is_unable() {

        for (int i = 0; i < cacheSize; i++) {
            TicketBox<?> ticketBox = boxManager.get(
                    new SegmentID("ticket_1")
            );
            assertNotNull(ticketBox);
            boxManager.release(ticketBox);
        }

        setRepository(new ExceptionTicketBoxRepositoryImpl());

        for (int i = 0; i < cacheSize; i++) {
            TicketBox<?> ticketBox = boxManager.get(
                    new SegmentID("ticket_1")
            );
            assertNotNull(ticketBox);
            boxManager.release(ticketBox);
        }

        TicketBox<?> ticketBox = boxManager.get(
                new SegmentID("ticket_1")
        );

        assertNull(ticketBox);

    }


    @Test
    public void should_get_and_released_box_thread_safe() throws InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        CountDownLatch countDownLatch = new CountDownLatch(3);
        Set<TicketBox<?>> boxSet = Collections.synchronizedSet(new HashSet<>());
        AtomicBoolean getNullBox = new AtomicBoolean(false);

        randomGetOrRelease(cyclicBarrier, countDownLatch, boxSet, getNullBox);
        randomGetOrRelease(cyclicBarrier, countDownLatch, boxSet, getNullBox);
        randomGetOrRelease(cyclicBarrier, countDownLatch, boxSet, getNullBox);

        countDownLatch.await();

        assertFalse(getNullBox.get(), "should not get null box");
    }

    private void setRepository(TicketSegmentRepository repository) {
        try {
            Field repositoryField = BoxManager.class.getDeclaredField("ticketSegmentRepository");
            repositoryField.setAccessible(true);
            repositoryField.set(boxManager, repository);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void randomGetOrRelease(CyclicBarrier cyclicBarrier,
                                    CountDownLatch countDownLatch,
                                    Set<TicketBox<?>> boxSet,
                                    AtomicBoolean getNullBox) {
        new ParallelThread(cyclicBarrier, countDownLatch, () -> {
            for (int i = 0; i < 5000; i++) {
                TicketBox<?> ticketBox = boxManager.get(new SegmentID(Math.random() > 0.5 ? "ticket_1" : "ticket_2"));

                if (ticketBox == null) {
                    getNullBox.set(true);
                    return;
                }

                if (Math.random() > 0.1) {
                    boxManager.release(ticketBox);
                    boxSet.add(ticketBox);
                }
            }
        }).start();
    }

    private void startGetBoxThread(CyclicBarrier cyclicBarrier,
                                   CountDownLatch countDownLatch,
                                   SegmentID segmentId,
                                   TicketBox<?>[] boxs,
                                   int index) {
        new ParallelThread(cyclicBarrier, countDownLatch, () -> {
            boxs[index] = boxManager.get(segmentId);
        }).start();
    }

    private Map<SegmentID, Queue<TicketBox<?>>> getTicketBoxQueue() {
        try {
            Field boxContainerField = BoxManager.class.getDeclaredField("boxContainer");
            boxContainerField.setAccessible(true);
            return (Map<SegmentID, Queue<TicketBox<?>>>) boxContainerField.get(boxManager);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    static class ExceptionTicketBoxRepositoryImpl implements TicketSegmentRepository {
        @Override
        public TicketSegment<?> ticketSegmentOf(SegmentID id) {
            throw new RuntimeException("mock exception");
        }

        @Override
        public void save(TicketSegment<?> ticketBox) {

        }

        @Override
        public void delete(SegmentID id) {

        }
    }

    static class MockTicketSegmentRepositoryImpl implements TicketSegmentRepository {
        private final Map<SegmentID, AutoIncrTicketSegment> dbMap = new HashMap<>() {{
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
            dbMap.put(ticketSegment.getId(), (AutoIncrTicketSegment) ticketSegment);
        }

        @Override
        public void delete(SegmentID id) {
            dbMap.remove(id);
        }

        private AutoIncrTicketSegment createTicketBox(SegmentID id) {
            return new AutoIncrTicketSegment(
                    id,
                    currentMaxTicket,
                    ticketNum,
                    "test desc"
            );
        }
    }
}