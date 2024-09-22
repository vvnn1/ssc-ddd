package indi.melon.ssc.domain.ticket.ticket;

import indi.melon.ssc.domain.ticket.exception.TicketBoxCreateFailException;
import indi.melon.ssc.domain.ticket.util.CyclicQueue;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vvnn1
 * @since 2024/5/25 14:17
 */
public class BoxManager {
    private final Map<BoxID, Queue<TicketBox<?>>> boxContainer;
    private final TicketBoxRepository ticketBoxRepository;
    private final int cacheSize;

    public BoxManager(TicketBoxRepository ticketBoxRepository) {
        this(ticketBoxRepository, 2);
    }

    public BoxManager(TicketBoxRepository ticketBoxRepository, int cacheSize) {
        this.boxContainer = new ConcurrentHashMap<>();
        this.cacheSize = cacheSize;
        this.ticketBoxRepository = ticketBoxRepository;
    }

    public void createBox(TicketBox<?> ticketBox) {
        try {
            ticketBoxRepository.create(ticketBox);
        }catch (Exception e){
            throw new TicketBoxCreateFailException(e);
        }
    }

    TicketBox<?> get(BoxID id) {
        Queue<TicketBox<?>> ticketBoxQueue = boxContainer.computeIfAbsent(id, this::createTicketBoxQueue);

        if (ticketBoxQueue == null) {
            return null;
        }

        return ticketBoxQueue.peek();
    }

    void release(TicketBox<?> box) {
        boxContainer.computeIfPresent(box.getId(), (id, ticketBoxes) -> {
            if (ticketBoxes.peek() != box) {
                return ticketBoxes;
            }

            ticketBoxes.poll();
            offer(ticketBoxes, id);
            return ticketBoxes;
        });
    }

    private CyclicQueue<TicketBox<?>> createTicketBoxQueue(BoxID id) {
        CyclicQueue<TicketBox<?>> ticketBoxes = new CyclicQueue<>(cacheSize);
        for (int i = 0; i < cacheSize; i++) {
            if (!offer(ticketBoxes, id)) {
                return null;
            }
        }

        return ticketBoxes;
    }

    private boolean offer(Queue<TicketBox<?>> queue, BoxID id) {
        try {
            TicketBox<?> ticketBox = ticketBoxRepository.get(id);
            if (ticketBox == null || !ticketBox.hasNext()) {
                return false;
            }

            if (ticketBoxRepository.update(ticketBox)) {
                return queue.offer(ticketBox);
            }
        }catch (Exception ignore){

        }
        return false;
    }
}
