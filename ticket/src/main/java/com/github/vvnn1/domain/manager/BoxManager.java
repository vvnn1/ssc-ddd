package com.github.vvnn1.domain.manager;

import com.github.vvnn1.domain.entity.TicketBox;
import com.github.vvnn1.domain.pojo.BoxID;
import com.github.vvnn1.domain.repository.TicketBoxRepository;
import com.github.vvnn1.domain.util.CyclicQueue;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vvnn1
 * @since 2024/5/25 14:17
 */
public class BoxManager {
    private final Map<BoxID, Queue<TicketBox<?>>> boxContainer;
    private final int cacheSize;
    private final TicketBoxRepository ticketBoxRepository;

    public BoxManager(TicketBoxRepository ticketBoxRepository, int cacheSize) {
        this.boxContainer = new ConcurrentHashMap<>();
        this.cacheSize = cacheSize;
        this.ticketBoxRepository = ticketBoxRepository;
    }

    public TicketBox<?> get(BoxID id) {
        Queue<TicketBox<?>> ticketBoxQueue = boxContainer.computeIfAbsent(id, this::createTicketBoxQueue);

        if (ticketBoxQueue == null) {
            return null;
        }

        if (ticketBoxQueue.size() != cacheSize) {
            throw new IllegalStateException("Illegal cache size, Shouldn't arrived here, please issue a bug");
        }

        return ticketBoxQueue.peek();
    }

    public void release(TicketBox<?> box) {
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

        if (ticketBoxes.size() == 0) {
            return null;
        }

        return ticketBoxes;
    }

    private boolean offer(Queue<TicketBox<?>> queue, BoxID id) {
        TicketBox<?> ticketBox = ticketBoxRepository.get(id);
        if (ticketBox == null) {
            return false;
        }
        return queue.offer(ticketBox);
    }
}
