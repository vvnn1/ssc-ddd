package com.github.vvnn1.domain.entity;

import com.github.vvnn1.domain.pojo.BoxID;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.*;

import java.time.LocalDateTime;
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
    private Queue<T> ticketQueue;

    public TicketBox() {
    }

    public TicketBox(BoxID id, T currentMaxTicket, Integer ticketNum) {
        this.id = id;
        this.currentMaxTicket = currentMaxTicket;
        this.ticketNum = ticketNum;
    }

    @Override
    public boolean hasNext() {
        return ticketQueue != null && ticketQueue.size() > 0;
    }

    @Override
    public T next() {
        if (ticketQueue == null) {
            throw new NullPointerException("ticket未初始化，获取前需先生成");
        }

        T ticket = ticketQueue.poll();
        if (ticket == null) {
            throw new NoSuchElementException("无可用ticket");
        }
        return ticket;
    }

    void fill() {
        if (ticketQueue == null) {
            this.ticketQueue = genTickets(id, currentMaxTicket, ticketNum);
        }
    }

    protected abstract ConcurrentLinkedQueue<T> genTickets(BoxID id, T currentMaxTicket, Integer ticketNums);
}
