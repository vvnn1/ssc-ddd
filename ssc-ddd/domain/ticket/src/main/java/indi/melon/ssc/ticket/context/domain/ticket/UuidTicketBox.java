package indi.melon.ssc.ticket.context.domain.ticket;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

/**
 * @author vvnn1
 * @since 2024/9/20 20:21
 */
public class UuidTicketBox extends TicketBox<String>{
    @Override
    protected Collection<String> genTickets(String currentMaxTicket, Integer ticketNums) {
        String[] tickets = new String[ticketNums];
        for (int i = 0; i < ticketNums; i++) {
            tickets[i] = UUID.randomUUID().toString();
        }
        return Arrays.asList(tickets);
    }

    @Override
    protected String incrMaxTicket(String currentMaxTicket, Integer ticketNums) {
        return currentMaxTicket;
    }
}
