package indi.melon.ssc.directory.domain.tree;

import indi.melon.ssc.directory.domain.south.MockTreeNodeRepository;
import indi.melon.ssc.directory.domain.tree.exception.NotSupportException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author wangmenglong
 * @since 2025/1/13 19:44
 */
public class TreeNodeManagerTest {
    private TreeNodeManager treeNodeManager;
    private MockTreeNodeRepository mockTreeNodeRepository;

    @BeforeEach
    public void setUp() throws Exception {
        mockTreeNodeRepository = new MockTreeNodeRepository();
        treeNodeManager = new TreeNodeManager(mockTreeNodeRepository, null);
    }

    @Test
    public void should_remove_tree_node_normal() {
        TreeNode rootNode = buildDirectoryNode(new NodeID("1"), true);

        TreeNode node2 = buildDirectoryNode(new NodeID("2"));
        rootNode.add(node2);

        TreeNode node3 = buildDirectoryNode(new NodeID("3"));
        rootNode.add(node3);

        TreeNode node4 = buildFileNode(new NodeID("4"));
        node3.add(node4);
        node3.locked();
        mockTreeNodeRepository.save(rootNode);

        assertFalse(treeNodeManager.removeTreeNode(new NodeID("notFound")));

        assertThrows(NotSupportException.class, () -> treeNodeManager.removeTreeNode(new NodeID("1")));

        assertTrue(treeNodeManager.removeTreeNode(new NodeID("2")));
        assertNull(mockTreeNodeRepository.treeNodeOf(new NodeID("2")));


    }

    private TreeNode buildDirectoryNode(NodeID id, boolean isRoot) {
        return new TreeNode(
                id,
                "treeNode" + id,
                "directory",
                true,
                isRoot
        );
    }

    private TreeNode buildDirectoryNode(NodeID id) {
        return buildDirectoryNode(id, false);
    }

    private TreeNode buildFileNode(NodeID id) {
        return new TreeNode(
                id,
                "treeNode" + id,
                "file",
                false,
                false
        );
    }
}
