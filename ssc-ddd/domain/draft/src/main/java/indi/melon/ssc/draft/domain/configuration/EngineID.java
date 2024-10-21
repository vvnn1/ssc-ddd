package indi.melon.ssc.draft.domain.configuration;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

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
public class EngineID implements Serializable {
    @Serial
    private static final long serialVersionUID = -1988552305225064567L;
    String value;
}
