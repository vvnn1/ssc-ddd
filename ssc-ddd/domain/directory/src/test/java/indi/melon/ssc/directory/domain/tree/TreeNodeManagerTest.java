package indi.melon.ssc.directory.domain.tree;

import indi.melon.ssc.directory.domain.south.MockTreeNodeFactory;
import indi.melon.ssc.directory.domain.south.MockTreeNodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author wangmenglong
 * @since 2025/1/13 19:44
 */
public class TreeNodeManagerTest {
    private TreeNodeManager treeNodeManager;

    @BeforeEach
    public void setUp() {
        treeNodeManager = new TreeNodeManager(new MockTreeNodeRepository(), new MockTreeNodeFactory("mock_"));
    }

    @Test
    public void should_create_root_node_normally() {
        TreeNode rootNode = treeNodeManager.createRootTreeNode(
                "root",
                "directory",
                true
        );

        assertEquals(new NodeID("mock_root"), rootNode.getId());
        assertEquals("root", rootNode.getName());
        assertEquals("directory", rootNode.getType());
        assertTrue(rootNode.getExpandable());
        assertFalse(rootNode.isLocked());
        assertTrue(rootNode.isRoot());
    }

    @Test
    public void should_create_node_under_right_parent() {
        TreeNode rootNode = treeNodeManager.createRootTreeNode(
                "root",
                "directory",
                true
        );

        TreeNode treeNode1 = treeNodeManager.createTreeNode(
                "treeNode1",
                "directory",
                true,
                rootNode.getId()
        );

        assertNotNull(rootNode.get(treeNode1.getId()));
        assertSame(rootNode.get(treeNode1.getId()), treeNode1);

        TreeNode treeNode2 = treeNodeManager.createTreeNode(
                "treeNode2",
                "file",
                false,
                treeNode1.getId()
        );

        assertNotNull(treeNode1.get(treeNode2.getId()));
        assertSame(treeNode1.get(treeNode2.getId()), treeNode2);
    }
}
