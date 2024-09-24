package indi.melon.ssc.domain.directory.tree;

import indi.melon.ssc.domain.directory.exception.IllegalTreeNodeException;
import indi.melon.ssc.domain.directory.exception.TreeNodeAlreadyExistException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

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
    public void should_throw_exception_when_add_same_node_to_same_parent() {
        TreeNode rootNode = buildTree(); // 0-11

        TreeNode childNode12 = buildNode(new NodeID("12"), new NodeID("3"));
        assertTrue(rootNode.add(childNode12));

        TreeNode childNode13 = buildNode(new NodeID("13"), new NodeID("0"));
        assertTrue(rootNode.add(childNode13));

        TreeNode childNode6 = buildNode(new NodeID("6"), new NodeID("11"));
        assertThrows(IllegalTreeNodeException.class, () -> rootNode.add(childNode6));

        TreeNode childNode14 = buildNameNode(new NodeID("14"), new NodeID("5"), "Node-14");
        assertTrue(rootNode.add(childNode14));

        TreeNode childNode14Copy = buildNameNode(new NodeID("14-1"), new NodeID("5"), "Node-14");
        assertThrows(TreeNodeAlreadyExistException.class, () -> rootNode.add(childNode14Copy));

        TreeNode childNode14File = buildNameNode(new NodeID("14-2"), new NodeID("5"), "Node-14");
        childNode14File.setType("file");
        assertTrue(rootNode.add(childNode14File));
    }

    @Test
    public void should_be_sorted_with_comparator() {
        TreeNode rootNode = buildNode(new NodeID("0"), null);

        List<TreeNode> childNodeList = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            TreeNode treeNode = buildTimeNode(new NodeID(String.valueOf(i)), new NodeID("0"), LocalDateTime.of(2024, new Random().nextInt(11) + 1, new Random().nextInt(29) + 1, new Random().nextInt(23), new Random().nextInt(59)));
            childNodeList.add(treeNode);
        }

        rootNode.setChildNodeList(childNodeList);

        assertFalse(isCreateTimeAsc(rootNode));

        rootNode.setComparator(Comparator.comparing(TreeNode::getCreateTime));
        assertTrue(isCreateTimeAsc(rootNode));


        TreeNode treeNode = buildTimeNode(new NodeID("7"), new NodeID("0"), LocalDateTime.of(2023, 1, 1, 1, 1));
        rootNode.add(treeNode);

        assertTrue(isCreateTimeAsc(rootNode));
    }

    @Test
    public void only_root_node_can_add_node() {
        TreeNode rootNode = buildNode(new NodeID("0"), null);
        TreeNode childNode1 = buildNode(new NodeID("1"), new NodeID("0"));

        assertTrue(rootNode.add(childNode1));

        TreeNode childNode2 = buildNode(new NodeID("2"), new NodeID("1"));
        assertThrows(IllegalTreeNodeException.class, () -> childNode1.add(childNode2));
    }

    private boolean isCreateTimeAsc(TreeNode rootNode) {
        LinkedList<TreeNode> queue = new LinkedList<>(Collections.singleton(rootNode));
        while (!queue.isEmpty()) {
            TreeNode treeNode = queue.remove();
            if (!isCreateTimeAsc(treeNode.getChildNodeList())){
                return false;
            }
            if (treeNode.getChildNodeList() != null){
                queue.addAll(treeNode.getChildNodeList());
            }
        }
        return true;
    }

    private boolean isCreateTimeAsc(List<TreeNode> treeNodeList) {
        TreeNode pre = null;
        for (TreeNode treeNode : treeNodeList) {
            if (pre != null && pre.getCreateTime().isAfter(treeNode.getCreateTime())){
                return false;
            }
            pre = treeNode;
        }

        return true;
    }

    private TreeNode buildTimeNode(NodeID id, NodeID parentId, LocalDateTime localDateTime) {
        TreeNode treeNode = buildNode(id, parentId);
        treeNode.setCreateTime(localDateTime);
        return treeNode;
    }

    private TreeNode buildNode(NodeID id, NodeID parentId) {
        TreeNode treeNode = new TreeNode();
        treeNode.setId(id);
        treeNode.setName("treeNode" + id);
        treeNode.setParentId(parentId);
        treeNode.setChildNodeList(new ArrayList<>());
        treeNode.setCreateTime(LocalDateTime.now());
        treeNode.setType("directory");
        return treeNode;
    }

    private TreeNode buildNameNode(NodeID id, NodeID parentId, String name) {
        TreeNode treeNode = buildNode(id, parentId);
        treeNode.setName(name);
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