package indi.melon.ssc.directory.domain.south;

import indi.melon.ssc.directory.domain.south.repository.TreeNodeRepository;
import indi.melon.ssc.directory.domain.tree.NodeID;
import indi.melon.ssc.directory.domain.tree.TreeNode;

import java.util.*;

/**
 * @author vvnn1
 * @since 2024/10/26 14:40
 */
public class MockTreeNodeRepository implements TreeNodeRepository {
    private final Map<NodeID, TreeNode> db = new HashMap<>();

    @Override
    public TreeNode treeNodeOf(NodeID id) {
        return db.get(id);
    }

    @Override
    public TreeNode save(TreeNode treeNode) {
        TreeNode rootNode = treeNode;
        while(!rootNode.isRoot()) {
            rootNode = rootNode.getParentNode();
        };

        db.clear();

        LinkedList<TreeNode> queue = new LinkedList<>(Collections.singletonList(rootNode));
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            db.put(node.getId(), node);

            if (node.getChildNodeList() != null && !node.getChildNodeList().isEmpty()) {
                queue.addAll(node.getChildNodeList());
            }
        }
        return treeNode;
    }

    @Override
    public void delete(NodeID id) {
        TreeNode treeNode = db.get(id);
        LinkedList<TreeNode> queue = new LinkedList<>(Collections.singletonList(treeNode));
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            db.remove(node.getId());
            if (node.getChildNodeList() != null && !node.getChildNodeList().isEmpty()) {
                queue.addAll(node.getChildNodeList());
            }
        }
    }
}
