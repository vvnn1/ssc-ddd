package com.github.vvnn1.domain.pojo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author vvnn1
 * @since 2024/4/13 12:45
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Embeddable
public class BoxID implements Serializable {
    private String bizTag;
}
