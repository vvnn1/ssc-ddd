package indi.melon.ssc.directory.south.repository;

import indi.melon.ssc.directory.domain.south.repository.TreeNodeRepository;
import indi.melon.ssc.directory.domain.tree.NodeID;
import indi.melon.ssc.directory.domain.tree.TreeNode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vvnn1
 * @since 2024/10/19 20:22
 */
public class MockTreeNodeRepository implements TreeNodeRepository {
    private final Map<NodeID, TreeNode> db = new HashMap<>();

    @Override
    public TreeNode treeNodeOf(NodeID id) {
        return db.get(id);
    }

    @Override
    public void save(TreeNode treeNode) {
        db.put(treeNode.getId(), treeNode);
    }

    @Override
    public void delete(NodeID id) {
        db.remove(id);
    }
}
