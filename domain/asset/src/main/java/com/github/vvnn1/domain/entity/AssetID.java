package com.github.vvnn1.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

/**
 * @author vvnn1
 * @since 2024/4/6 16:59
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class AssetID implements Serializable {
    private Long id;
}
