package indi.melon.ssc.directory.context.south.repository;

import indi.melon.ssc.directory.context.domain.tree.NodeID;
import indi.melon.ssc.directory.context.domain.tree.TreeNode;

/**
 * @author vvnn1
 * @since 2024/9/22 21:59
 */
public interface TreeNodeRepository {
    TreeNode treeNodeOf(NodeID id);
    void save(TreeNode treeNode);
}
