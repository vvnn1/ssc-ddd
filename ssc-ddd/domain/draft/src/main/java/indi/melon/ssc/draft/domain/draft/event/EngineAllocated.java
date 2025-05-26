package indi.melon.ssc.draft.domain.draft.event;

import indi.melon.ssc.domain.common.cqrs.AbstractDomainEvent;
import indi.melon.ssc.draft.domain.draft.DraftID;
import indi.melon.ssc.draft.domain.draft.EngineID;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author vvnn1
 * @since 2024/10/3 14:38
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class EngineAllocated extends AbstractDomainEvent {
    private final DraftID draftId;
    private final EngineID engineId;

    public EngineAllocated(DraftID draftId, EngineID engineId) {
        this.draftId = draftId;
        this.engineId = engineId;
    }

}
