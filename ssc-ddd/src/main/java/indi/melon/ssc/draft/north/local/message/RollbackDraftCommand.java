package indi.melon.ssc.draft.north.local.message;


import indi.melon.ssc.common.exception.ApplicationValidationException;

/**
 * @author wangmenglong
 * @since 2024/11/5 19:07
 */
public record RollbackDraftCommand(String draftId,
                                   String versionId,
                                   String modifier) {
    public RollbackDraftCommand {
        if (draftId == null || draftId.trim().isEmpty()) {
            throw new ApplicationValidationException("draftId can not be null or empty");
        }

        if (versionId == null || versionId.trim().isEmpty()) {
            throw new ApplicationValidationException("versionId can not be null or empty");
        }

        if (modifier == null || modifier.trim().isEmpty()) {
            throw new ApplicationValidationException("modifier can not be null or empty");
        }
    }
}
