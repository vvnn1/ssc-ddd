package indi.melon.ssc.draft.north.local.message;

import jakarta.annotation.Nonnull;

/**
 * @author wangmenglong
 * @since 2024/11/5 19:32
 */
public record RenameDraftCommand(@Nonnull String draftId,
                                 @Nonnull String newName,
                                 @Nonnull String modifier) {
}
