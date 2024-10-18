package indi.melon.ssc.domain.draft.version;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.*;


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
public class VersionID {
    @Id
    private String id;
}
