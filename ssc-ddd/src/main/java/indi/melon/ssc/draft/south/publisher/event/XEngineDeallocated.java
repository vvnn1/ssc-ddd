package indi.melon.ssc.draft.south.publisher.event;

import indi.melon.ssc.common.event.IntegrationEvent;
import lombok.Getter;

/**
 * @author wangmenglong
 * @since 2025/5/29 19:29
 */
@Getter
public class XEngineDeallocated extends IntegrationEvent {
    private final String draftId;
    private final String engineId;

    public XEngineDeallocated(String draftId, String engineId) {
        this.draftId = draftId;
        this.engineId = engineId;
    }
}
