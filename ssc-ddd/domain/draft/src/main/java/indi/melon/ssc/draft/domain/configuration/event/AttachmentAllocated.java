package indi.melon.ssc.draft.domain.configuration.event;

import indi.melon.ssc.draft.domain.configuration.AttachmentID;
import indi.melon.ssc.draft.domain.configuration.ConfigurationID;
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
