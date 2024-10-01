package indi.melon.ssc.context.directory.south.repository;

import indi.melon.ssc.context.directory.domain.tree.NodeID;
import indi.melon.ssc.context.directory.domain.tree.TreeNode;

/**
 * @author vvnn1
 * @since 2024/9/22 21:59
 */
public interface TreeNodeRepository {
    TreeNode treeNodeOf(NodeID id);
    void save(TreeNode treeNode);
}
