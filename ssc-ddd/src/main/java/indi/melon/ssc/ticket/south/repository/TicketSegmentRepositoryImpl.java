package indi.melon.ssc.ticket.south.repository;

import indi.melon.ssc.common.exception.ApplicationInfrastructureException;
import indi.melon.ssc.ticket.domain.south.repository.TicketSegmentRepository;
import indi.melon.ssc.ticket.domain.ticket.SegmentID;
import indi.melon.ssc.ticket.domain.ticket.TicketSegment;
import indi.melon.ssc.ticket.south.repository.dao.TicketSegmentDao;
import org.springframework.stereotype.Repository;

/**
 * @author vvnn1
 * @since 2024/10/16 21:47
 */
@Repository
public class TicketSegmentRepositoryImpl implements TicketSegmentRepository {
    private final TicketSegmentDao ticketSegmentDao;

    public TicketSegmentRepositoryImpl(TicketSegmentDao ticketSegmentDao) {
        this.ticketSegmentDao = ticketSegmentDao;
    }

    @Override
    public TicketSegment<?> ticketSegmentOf(SegmentID id) {
        try {
            return ticketSegmentDao.findById(id).orElse(null);
        }catch (Exception e){
            throw new ApplicationInfrastructureException("find ticket box by draftId: " + id + " failed.", e);
        }
    }

    @Override
    public void save(TicketSegment<?> ticketBox) {
        try {
            ticketSegmentDao.save(ticketBox);
        }catch (Exception e){
            throw new ApplicationInfrastructureException("save ticket box failed. box: " + ticketBox, e);
        }
    }

    @Override
    public void delete(SegmentID id) {
        try {
            ticketSegmentDao.deleteById(id);
        } catch (Exception e){
            throw new ApplicationInfrastructureException("remove ticket box [" + id + "] failed.", e);
        }
    }
}
