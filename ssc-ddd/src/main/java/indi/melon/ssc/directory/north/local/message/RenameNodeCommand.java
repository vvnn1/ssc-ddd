package indi.melon.ssc.directory.north.local.message;

import indi.melon.ssc.common.exception.ApplicationValidationException;
import indi.melon.ssc.domain.common.utils.StringUtils;
import jakarta.annotation.Nonnull;

/**
 * @author wangmenglong
 * @since 2024/10/18 16:25
 */
public record RenameNodeCommand(@Nonnull String rootNodeId,
                                @Nonnull TreeNode treeNode) {

    public record TreeNode(@Nonnull String id, @Nonnull String newName) {

    }
}
