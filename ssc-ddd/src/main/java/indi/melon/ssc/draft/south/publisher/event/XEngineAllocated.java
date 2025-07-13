package indi.melon.ssc.draft.south.publisher.event;

import indi.melon.ssc.common.event.IntegrationEvent;
import lombok.Getter;

/**
 * @author wangmenglong
 * @since 2025/5/26 17:40
 */
@Getter
public class XEngineAllocated extends IntegrationEvent {
    private final String draftId;
    private final String engineId;

    public XEngineAllocated(String draftId, String engineId) {
        this.draftId = draftId;
        this.engineId = engineId;
    }
}
