package indi.melon.ssc.ticket.south.repository;

import indi.melon.ssc.common.exception.ApplicationInfrastructureException;
import indi.melon.ssc.ticket.domain.south.repository.TicketBoxRepository;
import indi.melon.ssc.ticket.domain.ticket.BoxID;
import indi.melon.ssc.ticket.domain.ticket.TicketBox;
import indi.melon.ssc.ticket.south.repository.dao.TicketBoxDao;
import org.springframework.stereotype.Repository;

/**
 * @author vvnn1
 * @since 2024/10/16 21:47
 */
@Repository
public class TicketBoxRepositoryImpl implements TicketBoxRepository {
    private final TicketBoxDao ticketBoxDao;

    public TicketBoxRepositoryImpl(TicketBoxDao ticketBoxDao) {
        this.ticketBoxDao = ticketBoxDao;
    }

    @Override
    public TicketBox<?> ticketBoxOf(BoxID id) {
        try {
            return ticketBoxDao.findById(id).orElse(null);
        }catch (Exception e){
            throw new ApplicationInfrastructureException("find ticket box by id: " + id + " failed.", e);
        }
    }

    @Override
    public void save(TicketBox<?> ticketBox) {
        try {
            ticketBoxDao.save(ticketBox);
        }catch (Exception e){
            throw new ApplicationInfrastructureException("save ticket box failed. box: " + ticketBox, e);
        }
    }

    @Override
    public void delete(BoxID id) {
        try {
            ticketBoxDao.deleteById(id);
        } catch (Exception e){
            throw new ApplicationInfrastructureException("remove ticket box [" + id + "] failed.", e);
        }
    }
}
