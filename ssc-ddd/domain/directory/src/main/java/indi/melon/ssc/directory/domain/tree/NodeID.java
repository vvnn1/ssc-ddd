package indi.melon.ssc.directory.domain.tree;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author vvnn1
 * @since 2024/9/22 22:05
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class NodeID implements Serializable {
    @Serial
    private static final long serialVersionUID = 1749092202572956771L;
    String value;
}
