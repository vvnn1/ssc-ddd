package indi.melon.ssc.domain.directory.tree;

/**
 * @author vvnn1
 * @since 2024/9/22 21:59
 */
public interface TreeNodeRepository {
    TreeNode treeNodeOf(NodeID id);
    void save(TreeNode treeNode);
}
