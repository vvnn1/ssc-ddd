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
public final class AttachmentID implements Serializable {
    @Serial
    private static final long serialVersionUID = 6814732986037436521L;
    final String value;
}
