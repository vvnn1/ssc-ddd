package indi.melon.ssc.domain.ticket.ticket;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author vvnn1
 * @since 2024/9/20 20:13
 */
public class AutoIncrTicketBox extends TicketBox<Integer>{
    @Override
    protected Collection<Integer> genTickets(Integer currentMaxTicket, Integer ticketNums) {
        Integer[] tickets = new Integer[ticketNums];
        for (Integer i = 0; i < ticketNums; i++) {
            tickets[i] = currentMaxTicket + i + 1;
        }
        return Arrays.asList(tickets);
    }

    @Override
    protected Integer incrMaxTicket(Integer currentMaxTicket, Integer ticketNums) {
        return currentMaxTicket + ticketNums;
    }
}
