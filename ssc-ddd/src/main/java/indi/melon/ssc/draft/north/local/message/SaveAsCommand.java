package indi.melon.ssc.draft.north.local.message;

import jakarta.annotation.Nonnull;

/**
 * @author wangmenglong
 * @since 2024/10/31 20:20
 */
public record SaveAsCommand(
        @Nonnull String name,
        @Nonnull String fromDraftId,
        @Nonnull Directory directory,
        @Nonnull String creator

) {
    public record Directory(
            @Nonnull String rootId,
            @Nonnull String parentId
    ){

    }
}
