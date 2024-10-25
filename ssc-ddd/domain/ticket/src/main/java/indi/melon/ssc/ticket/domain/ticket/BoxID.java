package indi.melon.ssc.ticket.domain.ticket;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author vvnn1
 * @since 2024/4/13 12:45
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class BoxID implements Serializable {
    @Serial
    private static final long serialVersionUID = -6134017593036040140L;
    String bizTag;
}
