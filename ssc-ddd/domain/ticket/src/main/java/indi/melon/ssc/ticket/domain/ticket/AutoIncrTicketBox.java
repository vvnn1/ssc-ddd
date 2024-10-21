package indi.melon.ssc.ticket.domain.ticket;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author vvnn1
 * @since 2024/9/20 20:13
 */
@Getter
@Setter(AccessLevel.PACKAGE)
public class AutoIncrTicketBox extends TicketBox<Long>{

    private Long currentMaxTicket;

    public AutoIncrTicketBox() {
    }

    public AutoIncrTicketBox(BoxID id, Long currentMaxTicket, Integer ticketNum, String desc) {
        super(id, ticketNum, desc, TicketEnum.AUTO_INCREMENT);
        this.currentMaxTicket = currentMaxTicket;
    }

    @Override
    protected Collection<Long> genTickets(Integer ticketNum) {
        Long[] tickets = new Long[ticketNum];
        for (Integer i = 0; i < ticketNum; i++) {
            tickets[i] = currentMaxTicket + i + 1;
        }
        currentMaxTicket += ticketNum;
        return Arrays.asList(tickets);
    }
}
