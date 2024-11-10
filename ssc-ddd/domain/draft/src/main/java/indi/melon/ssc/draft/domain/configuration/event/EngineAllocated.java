package indi.melon.ssc.draft.domain.configuration.event;

import indi.melon.ssc.domain.common.cqrs.AbstractDomainEvent;
import indi.melon.ssc.draft.domain.configuration.ConfigurationID;
import indi.melon.ssc.draft.domain.configuration.EngineID;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author vvnn1
 * @since 2024/10/3 14:38
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class EngineAllocated extends AbstractDomainEvent {
    private final ConfigurationID configurationID;
    private final EngineID engineID;

    public EngineAllocated(ConfigurationID configurationID, EngineID engineID) {
        this.configurationID = configurationID;
        this.engineID = engineID;
    }

}
