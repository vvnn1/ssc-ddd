package indi.melon.ssc.domain.ticket.ticket;


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

    public <T> T require(BoxID id) {
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
