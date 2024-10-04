package indi.melon.ssc.draft.context.domain.configuration.event;

import indi.melon.ssc.domain.common.cqrs.AbstractDomainEvent;
import indi.melon.ssc.draft.context.domain.configuration.AttachmentID;
import indi.melon.ssc.draft.context.domain.configuration.ConfigurationID;

/**
 * @author vvnn1
 * @since 2024/10/3 14:36
 */
public class AttachmentDeallocated extends AbstractDomainEvent {
    private final ConfigurationID configurationId;
    private final AttachmentID attachmentId;

    public AttachmentDeallocated(ConfigurationID configurationId, AttachmentID attachmentId) {
        this.configurationId = configurationId;
        this.attachmentId = attachmentId;
    }

    public ConfigurationID getConfigurationId() {
        return configurationId;
    }

    public AttachmentID getAttachmentId() {
        return attachmentId;
    }
}
