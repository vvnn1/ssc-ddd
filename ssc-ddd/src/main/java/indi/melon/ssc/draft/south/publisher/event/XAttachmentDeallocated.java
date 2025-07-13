package indi.melon.ssc.draft.south.publisher.event;

import indi.melon.ssc.common.event.IntegrationEvent;
import lombok.Getter;

/**
 * @author wangmenglong
 * @since 2025/5/29 19:43
 */
@Getter
public class XAttachmentDeallocated extends IntegrationEvent {
    private final String draftId;
    private final String attachmentId;

    public XAttachmentDeallocated(String draftId, String attachmentId) {
        this.draftId = draftId;
        this.attachmentId = attachmentId;
    }
}
