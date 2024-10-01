package indi.melon.ssc.context.ticket.domain.ticket;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author vvnn1
 * @since 2024/4/13 12:45
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class BoxID implements Serializable {
    @Serial
    private static final long serialVersionUID = -6134017593036040140L;
    private String bizTag;
}
