package indi.melon.ssc.domain.directory.tree;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author vvnn1
 * @since 2024/9/22 22:05
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class NodeID implements Serializable {
    @Serial
    private static final long serialVersionUID = 1749092202572956771L;
    private String id;
}
