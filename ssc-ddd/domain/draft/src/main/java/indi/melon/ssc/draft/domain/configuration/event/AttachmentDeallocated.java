package indi.melon.ssc.draft.domain.configuration.event;

import indi.melon.ssc.domain.common.cqrs.AbstractDomainEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author vvnn1
 * @since 2024/10/3 14:36
 */

@Getter
@EqualsAndHashCode(callSuper = false)
public class AttachmentDeallocated extends AbstractDomainEvent {
    private final String draftId;
    private final String attachmentId;

    public AttachmentDeallocated(String draftId, String attachmentId) {
        this.draftId = draftId;
        this.attachmentId = attachmentId;
    }
}
