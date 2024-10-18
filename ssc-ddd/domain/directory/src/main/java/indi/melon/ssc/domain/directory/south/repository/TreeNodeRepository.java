package indi.melon.ssc.domain.directory.south.repository;

import indi.melon.ssc.domain.directory.tree.TreeNode;
import indi.melon.ssc.domain.directory.tree.NodeID;

/**
 * @author vvnn1
 * @since 2024/9/22 21:59
 */
public interface TreeNodeRepository {
    TreeNode treeNodeOf(NodeID id);
    void save(TreeNode treeNode);
    void remove(NodeID id);
}
