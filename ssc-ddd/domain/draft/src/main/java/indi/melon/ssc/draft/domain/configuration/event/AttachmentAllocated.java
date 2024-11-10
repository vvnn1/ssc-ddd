package indi.melon.ssc.draft.domain.configuration.event;

import indi.melon.ssc.domain.common.cqrs.AbstractDomainEvent;
import indi.melon.ssc.draft.domain.configuration.AttachmentID;
import indi.melon.ssc.draft.domain.configuration.ConfigurationID;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author vvnn1
 * @since 2024/10/3 14:36
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class AttachmentAllocated extends AbstractDomainEvent {
    private final ConfigurationID configurationId;
    private final AttachmentID attachmentId;

    public AttachmentAllocated(ConfigurationID configurationId, AttachmentID attachmentId) {
        this.configurationId = configurationId;
        this.attachmentId = attachmentId;
    }
}
