package com.github.vvnn1.domain.manager;

import com.github.vvnn1.domain.entity.TicketBox;
import com.github.vvnn1.domain.pojo.BoxID;
import com.github.vvnn1.domain.pojo.CreateTicketCommand;

import java.util.NoSuchElementException;

/**
 * @author vvnn1
 * @since 2024/4/9 13:26
 */
public class TicketManager {
    private final BoxManager boxManager;

    public TicketManager(BoxManager boxManager) {
        this.boxManager = boxManager;
    }

    public <T> T require(CreateTicketCommand<T> command) {
        for (;;){
            TicketBox<?> ticketBox = boxManager.get(
                    new BoxID(command.getBizTag())
            );

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
