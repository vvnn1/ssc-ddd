package com.github.vvnn1.domain.entity;

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
    private final Map<String, TicketBox<?>> ticketBoxMap = new ConcurrentHashMap<>();
    private final TicketBoxRepository ticketBoxRepository;

    public TicketManager(TicketBoxRepository ticketBoxRepository) {
        this.ticketBoxRepository = ticketBoxRepository;
    }

    public <T> T getTicket(CreateTicketCommand<T> command){
        TicketBox<?> ticketBox = ticketBoxMap.compute(command.getModule(), (ignore, box) -> {
            if (box == null || !box.hasNext()) {
                return ticketBoxRepository.genTicketBox(command.getModule(), command.getTicketClazz());
            }
            return box;
        });

        try{
            Object ticket = ticketBox.next();
            return (T) ticket;
        }catch (NoSuchElementException e){
            return getTicket(command);
        }
    }
}
