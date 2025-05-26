package indi.melon.ssc.draft.north.local.message;

import indi.melon.ssc.common.exception.ApplicationValidationException;

/**
 * @author wangmenglong
 * @since 2024/11/5 19:32
 */
public record RenameDraftCommand(String draftId,
                                 String newName,
                                 String modifier) {
    public RenameDraftCommand {
        if (draftId == null || draftId.isEmpty()) {
            throw new ApplicationValidationException("draftId can not be empty");
        }

        if (newName == null || newName.isEmpty()) {
            throw new ApplicationValidationException("newName can not be empty");
        }

        if (modifier == null || modifier.isEmpty()) {
            throw new ApplicationValidationException("modifier can not be empty");
        }
    }
}
