package com.github.vvnn1.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.*;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity
public abstract class TicketBox<T> implements Iterator<T> {
    @Id
    private Long id;
    private T currentMaxTicket;
    private Integer ticketNums;
    private String module;
    @Transient
    private Queue<T> ticketQueue;

    @Override
    public boolean hasNext() {
        return ticketQueue != null && ticketQueue.size() > 0;
    }

    @Override
    public T next() {
        if (ticketQueue == null){
            throw new NullPointerException("ticket未初始化，获取前需先生成");
        }

        T ticket = ticketQueue.poll();
        if (ticket == null){
            throw new NoSuchElementException("无可用ticket");
        }

        return ticket;
    }

    public void genTickets() {
        if (ticketQueue == null) {
            this.ticketQueue = genTicket(id, currentMaxTicket, ticketNums, module);
        }
    }

    protected abstract ConcurrentLinkedQueue<T> genTicket(Long id, T currentMaxTicket, Integer ticketNums, String module);
}
