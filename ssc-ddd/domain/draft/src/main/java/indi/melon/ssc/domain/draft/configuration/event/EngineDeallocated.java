package indi.melon.ssc.domain.draft.configuration.event;

import indi.melon.ssc.domain.draft.configuration.ConfigurationID;
import indi.melon.ssc.domain.draft.configuration.EngineID;
import indi.melon.ssc.domain.common.cqrs.AbstractDomainEvent;

/**
 * @author vvnn1
 * @since 2024/10/3 14:38
 */
public class EngineDeallocated extends AbstractDomainEvent {
    private final ConfigurationID configurationID;
    private final EngineID engineID;

    public EngineDeallocated(ConfigurationID configurationID, EngineID engineID) {
        this.configurationID = configurationID;
        this.engineID = engineID;
    }

    public ConfigurationID getConfigurationID() {
        return configurationID;
    }

    public EngineID getEngineID() {
        return engineID;
    }
}
