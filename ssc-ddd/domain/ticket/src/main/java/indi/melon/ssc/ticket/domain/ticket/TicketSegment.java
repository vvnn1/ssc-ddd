package indi.melon.ssc.ticket.domain.ticket;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author wangmenglong
 * @since 2025/3/27 17:30
 */
@Getter
@Setter(AccessLevel.PACKAGE)
public abstract class TicketSegment<T> {
    private SegmentID id;
    private Integer ticketNum;
    private String desc;
    private LocalDateTime updateTime;
    private TicketEnum type;
    private Integer version;

    public TicketSegment(SegmentID id, Integer ticketNum, String desc, TicketEnum type) {
        this.id = id;
        this.ticketNum = ticketNum;
        this.desc = desc;
        this.updateTime = LocalDateTime.now();
        this.type = type;
    }

    protected abstract Collection<T> genTickets(Integer ticketNums);

    Collection<T> genTickets() {
        return genTickets(ticketNum);
    }
}
