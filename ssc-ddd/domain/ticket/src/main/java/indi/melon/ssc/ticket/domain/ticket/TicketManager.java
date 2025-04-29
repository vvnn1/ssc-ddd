package indi.melon.ssc.ticket.domain.ticket;


import indi.melon.ssc.ticket.domain.south.repository.TicketSegmentRepository;
import indi.melon.ssc.ticket.domain.util.CyclicQueue;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author vvnn1
 * @since 2024/4/9 13:26
 */
public class TicketManager {
    private final BoxManager boxManager;

    public TicketManager(TicketSegmentRepository ticketSegmentRepository, int cacheSize) {
        this.boxManager = new BoxManager(ticketSegmentRepository, cacheSize);
    }

    public <T> T require(SegmentID id) {
        for (;;){
            TicketBox<?> ticketBox = boxManager.get(id);

            if (ticketBox == null) {
                return null;
            }

            if (ticketBox.hasNext()) {
                try {
                    Object ticket = ticketBox.next();
                    return (T) ticket;
                } catch (NoSuchElementException ignored) {

                }
            } else {
                boxManager.release(ticketBox);
            }
        }
    }
}

class BoxManager {
    private final Map<SegmentID, Queue<TicketBox<?>>> boxContainer;
    private final TicketSegmentRepository ticketSegmentRepository;
    private final int cacheSize;

    public BoxManager(TicketSegmentRepository ticketSegmentRepository) {
        this(ticketSegmentRepository, 2);
    }

    public BoxManager(TicketSegmentRepository ticketSegmentRepository, int cacheSize) {
        this.boxContainer = new ConcurrentHashMap<>();
        this.cacheSize = cacheSize;
        this.ticketSegmentRepository = ticketSegmentRepository;
    }

    TicketBox<?> get(SegmentID id) {
        Queue<TicketBox<?>> ticketBoxQueue = boxContainer.computeIfAbsent(id, this::createTicketBoxQueue);

        if (ticketBoxQueue == null) {
            return null;
        }

        return ticketBoxQueue.peek();
    }

    void release(TicketBox<?> box) {
        boxContainer.computeIfPresent(box.getSegmentId(), (id, ticketBoxes) -> {
            if (ticketBoxes.peek() != box) {
                return ticketBoxes;
            }

            ticketBoxes.poll();
            offer(ticketBoxes, id);
            return ticketBoxes;
        });
    }

    private CyclicQueue<TicketBox<?>> createTicketBoxQueue(SegmentID id) {
        CyclicQueue<TicketBox<?>> ticketBoxes = new CyclicQueue<>(cacheSize);
        for (int i = 0; i < cacheSize; i++) {
            if (!offer(ticketBoxes, id)) {
                return null;
            }
        }

        return ticketBoxes;
    }

    private boolean offer(Queue<TicketBox<?>> queue, SegmentID id) {
        try {
            TicketSegment<?> ticketSegment = ticketSegmentRepository.ticketSegmentOf(id);

            if (ticketSegment == null) {
                return false;
            }

            return queue.offer(
                    new TicketBox<>(ticketSegment)
            );
        }catch (Exception ignore){

        }
        return false;
    }
}

class TicketBox<T> implements Iterator<T> {
    private final Queue<T> ticketQueue;
    private final TicketSegment<T> ticketSegment;

    public TicketBox(TicketSegment<T> ticketSegment) {
        this.ticketQueue = new ConcurrentLinkedQueue<>(
                ticketSegment.genTickets()
        );
        this.ticketSegment = ticketSegment;
    }

    @Override
    public boolean hasNext() {
        return !ticketQueue.isEmpty();
    }

    public SegmentID getSegmentId() {
        return ticketSegment.getId();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return ticketQueue.remove();
    }
}

