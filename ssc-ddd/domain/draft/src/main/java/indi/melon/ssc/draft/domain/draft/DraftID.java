package indi.melon.ssc.draft.domain.draft;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author vvnn1
 * @since 2024/10/2 11:14
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class DraftID implements Serializable {
    @Serial
    private static final long serialVersionUID = 2868811892922001290L;
    private String value;
}
