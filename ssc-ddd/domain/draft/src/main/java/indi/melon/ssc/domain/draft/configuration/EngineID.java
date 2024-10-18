package indi.melon.ssc.domain.draft.configuration;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.*;

/**
 * @author vvnn1
 * @since 2024/10/2 12:15
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class EngineID {
    @Id
    private String id;
}
