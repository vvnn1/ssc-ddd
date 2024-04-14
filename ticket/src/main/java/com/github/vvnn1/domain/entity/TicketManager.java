package com.github.vvnn1.domain.entity;

import com.github.vvnn1.domain.pojo.BoxID;
import com.github.vvnn1.domain.pojo.CreateTicketCommand;
import com.github.vvnn1.domain.repository.TicketBoxRepository;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vvnn1
 * @since 2024/4/9 13:26
 */
public class TicketManager {
    private final Map<BoxID, TicketBox<?>> ticketBoxMap = new ConcurrentHashMap<>();
    private final TicketBoxRepository ticketBoxRepository;

    public TicketManager(TicketBoxRepository ticketBoxRepository) {
        this.ticketBoxRepository = ticketBoxRepository;
    }

    public <T> T getTicket(CreateTicketCommand<T> command) {
        TicketBox<?> ticketBox = ticketBoxMap.compute(command.getId(), (ignore, box) -> {
            if (box != null && box.hasNext()) {
                return box;
            }

            if (box != null) {
                ticketBoxRepository.expiry(box);
            }

            TicketBox<Object> newBox = ticketBoxRepository.get(command.getId());
            if (newBox != null) {
                newBox.fill();
            }
            return newBox;
        });

        if (ticketBox == null) {
            return null;
        }

        try {
            Object ticket = ticketBox.next();
            return (T) ticket;
        } catch (NoSuchElementException e) {
            return getTicket(command);
        }
    }

    public <T> void create(TicketBox<T> ticketBox) {
        ticketBoxRepository.create(ticketBox);
    }
}
