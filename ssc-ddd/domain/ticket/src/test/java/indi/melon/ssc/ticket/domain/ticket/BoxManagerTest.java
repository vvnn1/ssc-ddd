package indi.melon.ssc.ticket.domain.ticket;

import indi.melon.ssc.ticket.domain.south.repository.TicketBoxRepository;
import indi.melon.ssc.ticket.domain.thread.ParallelThread;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
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
    private TicketBoxRepository repository;
    private static final int cacheSize = 3;
    private static final long currentMaxTicket = 0L;
    private static final int ticketNum = 10;

    @BeforeEach
    public void init(){
        repository = new TestTicketBoxRepositoryImpl();
        boxManager = new BoxManager(repository, cacheSize);
    }

    @Test
    public void should_get_right_box_then_cache_some_box_and_update_currentMaxTicket() {
        assertNull(boxManager.get(
                new BoxID("ticket_3")
        ));

        BoxID boxID = new BoxID("ticket_1");
        TicketBox<?> ticketBox = boxManager.get(
                boxID
        );

        assertEquals(boxID, ticketBox.getId());
        Map<BoxID, Queue<TicketBox<?>>> map = getTicketBoxQueue();
        Queue<TicketBox<?>> boxQueue = map.get(boxID);
        assertEquals(boxQueue.size(), cacheSize);

        AutoIncrTicketBox dbBox = (AutoIncrTicketBox) repository.ticketBoxOf(boxID);
        assertEquals(dbBox.getCurrentMaxTicket(), currentMaxTicket + cacheSize * ticketNum);
    }

    @Test
    public void should_release_right_box_then_re_full_cache_and_update_currentMaxTicket(){
        BoxID boxID = new BoxID("ticket_1");
        TicketBox<?> ticketBox = boxManager.get(
                boxID
        );

        Map<BoxID, Queue<TicketBox<?>>> map = getTicketBoxQueue();
        Queue<TicketBox<?>> boxQueue = map.get(boxID);
        assertEquals(boxQueue.peek(), ticketBox);

        boxManager.release(ticketBox);
        assertNotEquals(boxQueue.peek(), ticketBox);
        assertEquals(boxQueue.size(), cacheSize);

        AutoIncrTicketBox dbBox = (AutoIncrTicketBox) repository.ticketBoxOf(boxID);
        assertEquals(dbBox.getCurrentMaxTicket(), currentMaxTicket + (cacheSize + 1) * ticketNum);

        TicketBox<?> peekBox = boxQueue.peek();
        boxManager.release(ticketBox);
        assertEquals(boxQueue.peek(), peekBox);

        AutoIncrTicketBox dbBox2 = (AutoIncrTicketBox) repository.ticketBoxOf(boxID);
        assertEquals(dbBox2.getCurrentMaxTicket(), currentMaxTicket + (cacheSize + 1) * ticketNum);
    }

    @Test
    public void should_get_same_box_although_multi_thread() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(4);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(4);

        BoxID boxID = new BoxID("ticket_1");
        final TicketBox<?>[] boxs = new TicketBox<?>[4];
        startGetBoxThread(cyclicBarrier, countDownLatch, boxID, boxs, 0);
        startGetBoxThread(cyclicBarrier, countDownLatch, boxID, boxs, 1);
        startGetBoxThread(cyclicBarrier, countDownLatch, boxID, boxs, 2);
        startGetBoxThread(cyclicBarrier, countDownLatch, boxID, boxs, 3);

        countDownLatch.await();

        assertSame(boxs[0], boxs[1]);
        assertSame(boxs[1], boxs[2]);
        assertSame(boxs[2], boxs[3]);
    }

    @Test
    public void should_enable_cache_when_repository_is_unable(){

        for (int i = 0; i < cacheSize; i++) {
            TicketBox<?> ticketBox = boxManager.get(
                    new BoxID("ticket_1")
            );
            assertNotNull(ticketBox);
            boxManager.release(ticketBox);
        }

        setRepository(new ExceptionTicketBoxRepositoryImpl());

        for (int i = 0; i < cacheSize; i++) {
            TicketBox<?> ticketBox = boxManager.get(
                    new BoxID("ticket_1")
            );
            assertNotNull(ticketBox);
            boxManager.release(ticketBox);
        }

        TicketBox<?> ticketBox = boxManager.get(
                new BoxID("ticket_1")
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

    @Test
    public void should_create_can_get_box_normally(){
        TicketBox<?> ticket_3 = boxManager.get(new BoxID("ticket_3"));
        assertNull(ticket_3);

        AutoIncrTicketBox autoIncrTicketBox = new AutoIncrTicketBox();
        autoIncrTicketBox.setId(new BoxID("ticket_3"));
        autoIncrTicketBox.setTicketNum(ticketNum);
        autoIncrTicketBox.setCurrentMaxTicket(currentMaxTicket);
        autoIncrTicketBox.setDesc("test desc");
        autoIncrTicketBox.setType(TicketEnum.AUTO_INCREMENT);
        autoIncrTicketBox.setUpdateTime(LocalDateTime.now());
        boxManager.createBox(autoIncrTicketBox);

        ticket_3 = boxManager.get(new BoxID("ticket_3"));
        assertNotNull(ticket_3);
    }

    private void setRepository(TicketBoxRepository repository){
        try {
            Field repositoryField = BoxManager.class.getDeclaredField("ticketBoxRepository");
            repositoryField.setAccessible(true);
            repositoryField.set(boxManager,repository);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void randomGetOrRelease(CyclicBarrier cyclicBarrier,
                                    CountDownLatch countDownLatch,
                                    Set<TicketBox<?>> boxSet,
                                    AtomicBoolean getNullBox){
        new ParallelThread(cyclicBarrier, countDownLatch, () -> {
            for (int i = 0; i < 5000; i++) {
                TicketBox<?> ticketBox = boxManager.get(new BoxID(Math.random() > 0.5 ? "ticket_1" : "ticket_2"));

                if (ticketBox == null){
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
                                   BoxID boxID,
                                   TicketBox<?>[] boxs,
                                   int index) {
        new ParallelThread(cyclicBarrier, countDownLatch, () -> {
            boxs[index] = boxManager.get(boxID);
        }).start();
    }

    private Map<BoxID, Queue<TicketBox<?>>> getTicketBoxQueue(){
        try {
            Field boxContainerField = BoxManager.class.getDeclaredField("boxContainer");
            boxContainerField.setAccessible(true);
            return (Map<BoxID, Queue<TicketBox<?>>>) boxContainerField.get(boxManager);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    static class ExceptionTicketBoxRepositoryImpl implements TicketBoxRepository{

        @Override
        public TicketBox<?> ticketBoxOf(BoxID id) {
            throw new RuntimeException("mock exception");
        }

        @Override
        public void save(TicketBox<?> ticketBox) {
        }

        @Override
        public void remove(BoxID id) {

        }
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
            TicketBox ticketBoxDB = new AutoIncrTicketBox();
            copy(ticketBox, ticketBoxDB);
            dbMap.put(ticketBoxDB.getId(), (AutoIncrTicketBox) ticketBoxDB);
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