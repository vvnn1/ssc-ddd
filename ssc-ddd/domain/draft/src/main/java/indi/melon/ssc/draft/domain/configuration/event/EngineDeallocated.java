package indi.melon.ssc.draft.domain.configuration.event;

import indi.melon.ssc.domain.common.cqrs.AbstractDomainEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author vvnn1
 * @since 2024/10/3 14:38
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class EngineDeallocated extends AbstractDomainEvent {
    private final String draftId;
    private final String engineId;

    public EngineDeallocated(String draftId, String engineId) {
        this.draftId = draftId;
        this.engineId = engineId;
    }
}
