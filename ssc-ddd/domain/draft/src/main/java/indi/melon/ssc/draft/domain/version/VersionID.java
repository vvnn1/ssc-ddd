package indi.melon.ssc.draft.domain.version;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;


/**
 * @author vvnn1
 * @since 2024/10/2 11:12
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class VersionID implements Serializable {
    @Serial
    private static final long serialVersionUID = 3061337027256099542L;
    String value;
}
