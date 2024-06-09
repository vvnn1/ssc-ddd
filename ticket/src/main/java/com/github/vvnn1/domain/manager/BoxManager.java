package com.github.vvnn1.domain.manager;

import com.github.vvnn1.domain.entity.TicketBox;
import com.github.vvnn1.domain.pojo.BoxID;
import com.github.vvnn1.domain.repository.TicketBoxRepository;
import com.github.vvnn1.domain.util.CyclicQueue;
import lombok.Getter;
import lombok.Setter;

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
    @Setter
    @Getter
    private int cacheSize;

    public BoxManager(TicketBoxRepository ticketBoxRepository) {
        this(ticketBoxRepository, 2);
    }

    public BoxManager(TicketBoxRepository ticketBoxRepository, int cacheSize) {
        this.boxContainer = new ConcurrentHashMap<>();
        this.cacheSize = cacheSize;
        this.ticketBoxRepository = ticketBoxRepository;
    }

    protected TicketBox<?> get(BoxID id) {
        Queue<TicketBox<?>> ticketBoxQueue = boxContainer.computeIfAbsent(id, this::createTicketBoxQueue);

        if (ticketBoxQueue == null) {
            return null;
        }

        return ticketBoxQueue.peek();
    }

    protected void release(TicketBox<?> box) {
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
        TicketBox<?> ticketBox = ticketBoxRepository.get(id);
        if (ticketBox == null) {
            return false;
        }
        return queue.offer(ticketBox);
    }
}
