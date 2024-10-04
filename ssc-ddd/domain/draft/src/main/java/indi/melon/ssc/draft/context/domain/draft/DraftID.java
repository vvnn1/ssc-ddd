package indi.melon.ssc.draft.context.domain.draft;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.*;

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
public class DraftID {
    @Id
    private String id;
}
