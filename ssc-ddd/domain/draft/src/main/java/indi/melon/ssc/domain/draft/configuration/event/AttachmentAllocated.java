package indi.melon.ssc.domain.draft.configuration.event;

import indi.melon.ssc.domain.draft.configuration.AttachmentID;
import indi.melon.ssc.domain.draft.configuration.ConfigurationID;
import indi.melon.ssc.domain.common.cqrs.AbstractDomainEvent;

/**
 * @author vvnn1
 * @since 2024/10/3 14:36
 */
public class AttachmentAllocated extends AbstractDomainEvent {
    private final ConfigurationID configurationId;
    private final AttachmentID attachmentId;

    public AttachmentAllocated(ConfigurationID configurationId, AttachmentID attachmentId) {
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
