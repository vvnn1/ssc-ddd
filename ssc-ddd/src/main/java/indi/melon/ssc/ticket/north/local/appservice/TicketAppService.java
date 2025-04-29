package indi.melon.ssc.ticket.north.local.appservice;

import indi.melon.ssc.common.exception.ApplicationDomainException;
import indi.melon.ssc.ticket.domain.ticket.SegmentID;
import indi.melon.ssc.ticket.domain.ticket.TicketManager;
import indi.melon.ssc.ticket.north.local.message.TicketCreateCommand;
import org.springframework.stereotype.Service;

/**
 * @author wangmenglong
 * @since 2024/10/17 19:39
 */
@Service
public class TicketAppService {

    private final TicketManager ticketManager;

    public TicketAppService(TicketManager ticketManager) {
        this.ticketManager = ticketManager;
    }

    public <T> T require(TicketCreateCommand<T> command) {
        String bizTag = command.bizTag();
        Class<T> clazz = command.clazz();

        try {
            Object ticket = ticketManager.require(
                    new SegmentID(bizTag)
            );
            return clazz.cast(ticket);
        }catch (Exception e){
            throw new ApplicationDomainException("generate ticket failed. command:" + command, e);
        }
    }
}
