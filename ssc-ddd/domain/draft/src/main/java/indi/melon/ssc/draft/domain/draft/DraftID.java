package indi.melon.ssc.draft.domain.draft;

import java.io.Serializable;

/**
 * @author vvnn1
 * @since 2024/10/2 11:14
 */
public record DraftID(String value) implements Serializable {
    @Override
    public String toString() {
        return value;
    }
}
