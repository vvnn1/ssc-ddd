package indi.melon.ssc.directory.domain.south.repository;

import indi.melon.ssc.directory.domain.tree.NodeID;
import indi.melon.ssc.directory.domain.tree.TreeNode;

/**
 * @author vvnn1
 * @since 2024/9/22 21:59
 */
public interface TreeNodeRepository {
    TreeNode treeNodeOf(NodeID id);
    void save(TreeNode treeNode);
    void remove(NodeID id);
}
