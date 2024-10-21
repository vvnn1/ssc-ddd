package indi.melon.ssc.ticket.domain.ticket;


import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

/**
 * @author vvnn1
 * @since 2024/9/20 20:21
 */
public class UuidTicketBox extends TicketBox<String>{
    public UuidTicketBox() {
    }

    public UuidTicketBox(BoxID id, Integer ticketNum, String desc) {
        super(id, ticketNum, desc, TicketEnum.UUID);
    }

    @Override
    protected Collection<String> genTickets(Integer ticketNums) {
        String[] tickets = new String[ticketNums];
        for (int i = 0; i < ticketNums; i++) {
            tickets[i] = UUID.randomUUID().toString();
        }
        return Arrays.asList(tickets);
    }
}
