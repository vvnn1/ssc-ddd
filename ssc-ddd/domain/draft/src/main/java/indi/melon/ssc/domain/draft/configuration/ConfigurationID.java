package indi.melon.ssc.domain.draft.configuration;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.*;

/**
 * @author vvnn1
 * @since 2024/10/2 11:15
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ConfigurationID {
    @Id
    private String id;
}
