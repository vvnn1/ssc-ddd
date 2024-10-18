package indi.melon.ssc.directory.north.local.message;

import lombok.Getter;

/**
 * @author vvnn1
 * @since 2024/10/5 12:51
 */
public record TreeNodeCreateCommand(
        String rootNodeId,
        TreeNode treeNode
) {
    public record TreeNode(
            String name,
            String type,
            Boolean expandable,
            String parentNodeId
    ) {

    }

}

