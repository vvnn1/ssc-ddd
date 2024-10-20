package indi.melon.ssc.draft.domain.configuration.event;

import indi.melon.ssc.draft.domain.configuration.ConfigurationID;
import indi.melon.ssc.draft.domain.configuration.EngineID;
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
    private final String configurationID;
    private final String engineID;

    public EngineDeallocated(String configurationID, String engineID) {
        this.configurationID = configurationID;
        this.engineID = engineID;
    }
}
