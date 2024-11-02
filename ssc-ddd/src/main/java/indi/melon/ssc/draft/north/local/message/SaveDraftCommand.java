package indi.melon.ssc.draft.north.local.message;

import jakarta.annotation.Nonnull;

/**
 * @author wangmenglong
 * @since 2024/11/2 11:12
 */
public record SaveDraftCommand(@Nonnull String draftId,
                               @Nonnull String content,
                               @Nonnull String modifier) {

}
