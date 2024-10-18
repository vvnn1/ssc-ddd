package indi.melon.ssc.domain.ticket.ticket;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author vvnn1
 * @since 2024/4/9 13:41
 */
@Getter
@Setter
@ToString
@Entity
public abstract class TicketBox<T> implements Iterator<T> {
    @EmbeddedId
    private BoxID id;
    private T currentMaxTicket;
    private Integer ticketNum;
    private String desc;
    private LocalDateTime updateTime;
    private TicketEnum type;
    @Transient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private volatile Queue<T> ticketQueue;

    public TicketBox() {
    }

    @Override
    public boolean hasNext() {
        if (ticketQueue == null){
            synchronized (this){
                if (ticketQueue == null){
                    Collection<T> tickets = genTickets(currentMaxTicket, ticketNum);
                    this.currentMaxTicket = incrMaxTicket(currentMaxTicket, ticketNum);
                    this.ticketQueue = new ConcurrentLinkedQueue<>(tickets);
                }
            }
        }
        return ticketQueue.size() > 0;
    }

    protected abstract Collection<T> genTickets(T currentMaxTicket, Integer ticketNums);

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return ticketQueue.remove();
    }

    protected abstract T incrMaxTicket(T currentMaxTicket, Integer ticketNums);
}
