package indi.melon.ssc.ticket.domain.ticket;

import jakarta.persistence.*;
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
@Entity
@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter(AccessLevel.PACKAGE)
@ToString
public abstract class TicketBox<T> implements Iterator<T> {
    @EmbeddedId
    private BoxID id;
    private Integer ticketNum;
    private String desc;
    private LocalDateTime updateTime;
    @Column(updatable = false, insertable = false)
    @Enumerated(value = EnumType.STRING)
    private TicketEnum type;
    @Transient
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
