package com.github.vvnn1.domain.pojo;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

/**
 * @author vvnn1
 * @since 2024/4/13 12:45
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class BoxID implements Serializable {
    private String bizTag;
}
