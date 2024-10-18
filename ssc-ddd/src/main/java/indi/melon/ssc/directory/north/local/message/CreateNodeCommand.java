package indi.melon.ssc.directory.north.local.message;


import indi.melon.ssc.common.exception.ApplicationValidationException;
import jakarta.annotation.Nonnull;

/**
 * @author vvnn1
 * @since 2024/10/5 12:51
 */
public record CreateNodeCommand(String rootNodeId,
                                @Nonnull TreeNode treeNode) {

    public CreateNodeCommand {
        if (rootNodeId == null && treeNode.parentNodeId != null){
            throw new ApplicationValidationException("please assign rootNodeId");
        }

        if (rootNodeId != null && treeNode.parentNodeId == null){
            throw new ApplicationValidationException("please assign parentNodeId");
        }
    }

    public Boolean isCreateRootNode() {
        return rootNodeId == null;
    }

    public record TreeNode(@Nonnull String name,
                           String type,
                           @Nonnull Boolean expandable,
                           String parentNodeId) {
    }

}

