package indi.melon.ssc.resource.domain.file;

import lombok.*;

import java.io.Serializable;

/**
 * @author wangmenglong
 * @since 2025/3/31 14:55
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class FileID implements Serializable {
    String value;
}
