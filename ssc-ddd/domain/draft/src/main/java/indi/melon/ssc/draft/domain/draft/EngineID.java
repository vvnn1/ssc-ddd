package indi.melon.ssc.draft.domain.draft;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author vvnn1
 * @since 2024/10/2 12:15
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public final class EngineID implements Serializable {
    final String value;
}
