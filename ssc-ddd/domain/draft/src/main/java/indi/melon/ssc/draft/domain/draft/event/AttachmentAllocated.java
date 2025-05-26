package indi.melon.ssc.draft.domain.draft.event;

import indi.melon.ssc.domain.common.cqrs.AbstractDomainEvent;
import indi.melon.ssc.draft.domain.draft.AttachmentID;
import indi.melon.ssc.draft.domain.draft.DraftID;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author vvnn1
 * @since 2024/10/3 14:36
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class AttachmentAllocated extends AbstractDomainEvent {
    private final DraftID draftId;
    private final AttachmentID attachmentId;

    public AttachmentAllocated(DraftID draftId, AttachmentID attachmentId) {
        this.draftId = draftId;
        this.attachmentId = attachmentId;
    }
}
