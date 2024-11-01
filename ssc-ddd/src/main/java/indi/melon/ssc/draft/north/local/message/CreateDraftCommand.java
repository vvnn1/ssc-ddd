package indi.melon.ssc.draft.north.local.message;


import jakarta.annotation.Nonnull;

/**
 * @author vvnn1
 * @since 2024/10/27 21:27
 */
public record CreateDraftCommand(@Nonnull String name,
                                 @Nonnull String templateId,
                                 @Nonnull String engineId,
                                 @Nonnull String creator,
                                 @Nonnull Directory directory) {
    public record Directory(
        @Nonnull String rootId,
        @Nonnull String parentId
    ){

    }
}
