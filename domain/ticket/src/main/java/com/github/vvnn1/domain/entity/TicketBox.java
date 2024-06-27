package com.github.vvnn1.domain.entity;

import com.github.vvnn1.domain.pojo.BoxID;
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
    @Transient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private volatile Queue<T> ticketQueue;

    public TicketBox() {
    }

    public TicketBox(BoxID id, T currentMaxTicket, Integer ticketNum) {
        this.id = id;
        this.currentMaxTicket = currentMaxTicket;
        this.ticketNum = ticketNum;
    }

    @Override
    public boolean hasNext() {
        if (ticketQueue == null){
            synchronized (this){
                if (ticketQueue == null){
                    Collection<T> tickets = genTickets(id, currentMaxTicket, ticketNum);
                    this.ticketQueue = new ConcurrentLinkedQueue<>(tickets);
                }
            }
        }
        return ticketQueue.size() > 0;
    }

    @Override
    public T next() {
        if (ticketQueue == null && !hasNext()) {
            throw new NoSuchElementException();
        }
        return ticketQueue.remove();
    }

    protected abstract Collection<T> genTickets(BoxID id, T currentMaxTicket, Integer ticketNums);
}
