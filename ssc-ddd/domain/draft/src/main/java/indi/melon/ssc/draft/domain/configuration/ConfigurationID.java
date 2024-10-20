package indi.melon.ssc.draft.domain.configuration;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

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
public class ConfigurationID implements Serializable {
    @Serial
    private static final long serialVersionUID = 7870459290823908190L;
    String value;
}
