package indi.melon.ssc.draft.domain.configuration;

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
@NoArgsConstructor
public class AttachmentID implements Serializable {
    @Serial
    private static final long serialVersionUID = 6814732986037436521L;
    String value;
}
