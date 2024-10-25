package indi.melon.ssc.ticket.domain.ticket;

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
@Setter(AccessLevel.PACKAGE)
public abstract class TicketBox<T> implements Iterator<T> {
    private BoxID id;
    private Integer ticketNum;
    private String desc;
    private LocalDateTime updateTime;
    private TicketEnum type;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private volatile Queue<T> ticketQueue;

    public TicketBox() {
    }

    public TicketBox(BoxID id, Integer ticketNum, String desc, TicketEnum type) {
        this.id = id;
        this.ticketNum = ticketNum;
        this.desc = desc;
        this.updateTime = LocalDateTime.now();
        this.type = type;
    }

    @Override
    public boolean hasNext() {
        if (!hasGenTickets()){
            synchronized (this){
                if (!hasGenTickets()){
                    fillTickets();
                }
            }
        }
        return !ticketQueue.isEmpty();
    }

    private void fillTickets() {
        Collection<T> tickets = genTickets(ticketNum);
        ticketQueue = new ConcurrentLinkedQueue<>(tickets);
        updateTime = LocalDateTime.now();
    }

    boolean hasGenTickets(){
        return ticketQueue != null;
    }

    protected abstract Collection<T> genTickets(Integer ticketNums);

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return ticketQueue.remove();
    }

}
