package indi.melon.ssc.directory.north.local.message;

import jakarta.annotation.Nonnull;

/**
 * @author wangmenglong
 * @since 2024/10/18 17:00
 */
public record DropNodeCommand(@Nonnull String rootNodeId, @Nonnull TreeNode treeNode) {
    public record TreeNode(@Nonnull String id) {

    }
}
