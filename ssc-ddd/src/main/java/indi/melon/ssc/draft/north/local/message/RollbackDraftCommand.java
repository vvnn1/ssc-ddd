package indi.melon.ssc.draft.north.local.message;

import jakarta.annotation.Nonnull;

/**
 * @author wangmenglong
 * @since 2024/11/5 19:07
 */
public record RollbackDraftCommand(@Nonnull String draftId,
                                   @Nonnull String versionId,
                                   @Nonnull String modifier) {
}
