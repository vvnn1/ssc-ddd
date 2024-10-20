package indi.melon.ssc.directory.north.local.message;

import jakarta.annotation.Nonnull;

/**
 * @author vvnn1
 * @since 2024/10/20 16:10
 */
public record MoveNodeCommand(@Nonnull String rootNodeId,
                              @Nonnull TreeNode treeNode) {

    public record TreeNode(@Nonnull String id,
                           @Nonnull String newParentId) {

    }
}
