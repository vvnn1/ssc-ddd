package indi.melon.ssc.draft.south.publisher.event;

import org.mapstruct.Named;

/**
 * @author wangmenglong
 * @since 2025/5/29 19:35
 */
public interface XCommonConverter {
    @Named("toString")
    default String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }
}
