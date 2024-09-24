package indi.melon.ssc.domain.directory.tree;

import indi.melon.ssc.domain.directory.exception.IllegalTreeNodeException;
import indi.melon.ssc.domain.directory.exception.TreeNodeAlreadyExistException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vvnn1
 * @since 2024/9/22 22:18
 */
class TreeNodeTest {

    @Test
    public void should_has_right_parent_node_when_be_added(){
        TreeNode rootNode = buildNode(new NodeID("0"), null);
        TreeNode childNode1 = buildNode(new NodeID("1"), new NodeID("0"));

        assertTrue(rootNode.add(childNode1));
        assertEquals(1, rootNode.getChildNodeList().size());
        assertEquals(childNode1, rootNode.getChildNodeList().get(0));

        TreeNode childNode2 = buildNode(new NodeID("2"), new NodeID("1"));
        assertTrue(rootNode.add(childNode2));
        assertEquals(1, childNode1.getChildNodeList().size());
        assertEquals(childNode2, childNode1.getChildNodeList().get(0));

        TreeNode childNode3 = buildNode(new NodeID("3"), new NodeID("4"));
        assertFalse(rootNode.add(childNode3));
        assertEquals(1, rootNode.getChildNodeList().size());
        assertEquals(1, childNode1.getChildNodeList().size());
        assertEquals(0, childNode2.getChildNodeList().size());

        TreeNode childNode4 = buildNode(new NodeID("4"), null);
        assertThrows(IllegalTreeNodeException.class, () -> rootNode.add(childNode4));
    }
    
    @Test
    public void should_throw_exception_when_add_exist_node() {
        TreeNode rootNode = buildTree();

        TreeNode childNode12 = buildNode(new NodeID("12"), new NodeID("3"));
        assertTrue(rootNode.add(childNode12));

        TreeNode childNode13 = buildNode(new NodeID("13"), new NodeID("0"));
        assertTrue(rootNode.add(childNode13));

        TreeNode childNode6 = buildNode(new NodeID("6"), new NodeID("5"));
        assertThrows(TreeNodeAlreadyExistException.class, () -> rootNode.add(childNode6));

        TreeNode childNode7 = buildNode(new NodeID("7"), new NodeID("3"));
        assertThrows(TreeNodeAlreadyExistException.class, () -> rootNode.add(childNode7));
    }

    @Test
    public void should_has_sorted_child_nodes() {

    }

    private TreeNode buildNode(NodeID id, NodeID parentId) {
        TreeNode treeNode = new TreeNode();
        treeNode.setId(id);
        treeNode.setName("treeNode" + id);
        treeNode.setParentId(parentId);
        treeNode.setChildNodeList(new ArrayList<>());
        return treeNode;
    }

    private TreeNode buildTree(){
        TreeNode rootNode = buildNode(new NodeID("0"), null);

        TreeNode childNode1 = buildNode(new NodeID("1"), new NodeID("0"));
        TreeNode childNode2 = buildNode(new NodeID("2"), new NodeID("0"));
        TreeNode childNode3 = buildNode(new NodeID("3"), new NodeID("0"));
        TreeNode childNode4 = buildNode(new NodeID("4"), new NodeID("0"));
        TreeNode childNode5 = buildNode(new NodeID("5"), new NodeID("0"));

        rootNode.setChildNodeList(new ArrayList<>(Arrays.asList(childNode1,childNode2,childNode3,childNode4,childNode5)));

        TreeNode childNode6 = buildNode(new NodeID("6"), new NodeID("5"));
        TreeNode childNode7 = buildNode(new NodeID("7"), new NodeID("5"));
        TreeNode childNode8 = buildNode(new NodeID("8"), new NodeID("5"));
        TreeNode childNode9 = buildNode(new NodeID("9"), new NodeID("4"));
        TreeNode childNode10 = buildNode(new NodeID("10"), new NodeID("4"));
        TreeNode childNode11 = buildNode(new NodeID("11"), new NodeID("3"));
        childNode5.setChildNodeList(new ArrayList<>(Arrays.asList(childNode6, childNode7, childNode8)));
        childNode4.setChildNodeList(new ArrayList<>(Arrays.asList(childNode9, childNode10)));
        childNode3.setChildNodeList(new ArrayList<>(List.of(childNode11)));

        return rootNode;
    }
}