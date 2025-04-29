package indi.melon.ssc.resource.domain.resource;

import lombok.*;

import java.io.Serializable;

/**
 * @author wangmenglong
 * @since 2025/3/31 11:24
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ResourceID implements Serializable {
    Long value;
}
