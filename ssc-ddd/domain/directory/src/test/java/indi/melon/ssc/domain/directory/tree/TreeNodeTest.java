package indi.melon.ssc.domain.directory.tree;

import indi.melon.ssc.domain.directory.exception.IllegalNodeException;
import indi.melon.ssc.domain.directory.exception.NodeAlreadyExistException;
import indi.melon.ssc.domain.directory.exception.NodeNotSupportException;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.*;

import static indi.melon.ssc.domain.directory.tree.Order.*;
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
        assertEquals(childNode1.getId(), rootNode.getChildNodeList().get(0).getId());

        TreeNode childNode2 = buildNode(new NodeID("2"), new NodeID("1"));
        assertTrue(rootNode.add(childNode2));
        assertEquals(1, childNode1.getChildNodeList().size());
        assertEquals(childNode2.getId(), childNode1.getChildNodeList().get(0).getId());

        TreeNode childNode3 = buildNode(new NodeID("3"), new NodeID("4"));
        assertFalse(rootNode.add(childNode3));
        assertEquals(1, rootNode.getChildNodeList().size());
        assertEquals(1, childNode1.getChildNodeList().size());
        assertEquals(0, childNode2.getChildNodeList().size());

        TreeNode childNode4 = buildNode(new NodeID("4"), null);
        assertThrows(IllegalNodeException.class, () -> rootNode.add(childNode4));
    }
    
    @Test
    public void should_throw_exception_when_add_same_node_to_same_parent() {
        TreeNode rootNode = buildTree(); // 0-11

        TreeNode childNode12 = buildNode(new NodeID("12"), new NodeID("3"));
        assertTrue(rootNode.add(childNode12));

        TreeNode childNode13 = buildNode(new NodeID("13"), new NodeID("0"));
        assertTrue(rootNode.add(childNode13));

        TreeNode childNode14 = buildNameNode(new NodeID("14"), new NodeID("5"), "Node-14");
        assertTrue(rootNode.add(childNode14));

        TreeNode childNode14Copy = buildNameNode(new NodeID("14-1"), new NodeID("5"), "Node-14");
        assertThrows(NodeAlreadyExistException.class, () -> rootNode.add(childNode14Copy));

        TreeNode childNode14File = buildNameNode(new NodeID("14-2"), new NodeID("5"), "Node-14");
        childNode14File.setType("file");
        assertTrue(rootNode.add(childNode14File));
    }

    @Test
    public void should_all_be_sorted_with_order() {
        TreeNode rootNode = buildNode(new NodeID("0"), null);

        List<TreeNode> childNodeList = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            TreeNode treeNode = buildTimeNode(new NodeID(String.valueOf(i)), new NodeID("0"), LocalDateTime.of(2024, new Random().nextInt(11) + 1, new Random().nextInt(29) + 1, new Random().nextInt(23), new Random().nextInt(59)));
            childNodeList.add(treeNode);
        }

        TreeNode treeNode8 = buildTimeNode(new NodeID("8"), new NodeID("0"), LocalDateTime.of(2024, new Random().nextInt(11) + 1, new Random().nextInt(29) + 1, new Random().nextInt(23), new Random().nextInt(59)));
        childNodeList.add(treeNode8);


        List<TreeNode> secondChildNodeList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TreeNode treeNode = buildTimeNode(new NodeID(String.valueOf(i+10)), new NodeID("8"), LocalDateTime.of(2024, new Random().nextInt(11) + 1, new Random().nextInt(29) + 1, new Random().nextInt(23), new Random().nextInt(59)));
            secondChildNodeList.add(treeNode);
        }

        treeNode8.setChildNodeList(secondChildNodeList);
        rootNode.setChildNodeList(childNodeList);

        assertFalse(isCreateTimeAsc(rootNode));

        rootNode.setOrder(orderBy(createTime, asc));
        assertTrue(isCreateTimeAsc(rootNode));


        TreeNode treeNode = buildTimeNode(new NodeID("7"), new NodeID("0"), LocalDateTime.of(2023, 1, 1, 1, 1));
        rootNode.add(treeNode);

        assertTrue(isCreateTimeAsc(rootNode));
    }

    @Test
    public void should_set_right_child_node_list() {
        TreeNode treeNode = buildNode(new NodeID("0"), null);

        TreeNode treeNode1 = buildNode(new NodeID("1"), new NodeID("0"));
        treeNode1.setExpandable(false);
        assertDoesNotThrow(() -> {
            treeNode.setChildNodeList(List.of(
                    treeNode1
            ));
        });

        assertThrows(IllegalNodeException.class, () -> {
            treeNode.setChildNodeList(List.of(
                    buildNode(new NodeID("2"), new NodeID("3"))
            ));
        });


        assertThrows(NodeNotSupportException.class, () -> {
            treeNode1.setChildNodeList(List.of(buildNode(new NodeID("4"), new NodeID("1"))));
        });
    }

    @Test
    public void only_root_node_can_invoke_add() {
        TreeNode rootNode = buildNode(new NodeID("0"), null);
        TreeNode childNode1 = buildNode(new NodeID("1"), new NodeID("0"));

        assertTrue(rootNode.add(childNode1));

        TreeNode childNode2 = buildNode(new NodeID("2"), new NodeID("1"));
        assertThrows(NodeNotSupportException.class, () -> childNode1.add(childNode2));
    }

    @Test
    public void can_be_parent_node_when_it_is_expandable(){
        TreeNode rootNode = buildNode(new NodeID("0"), null);
        TreeNode childNode1 = buildNode(new NodeID("1"), new NodeID("0"));
        assertTrue(rootNode.add(childNode1));

        TreeNode childNode2 = buildUnExpandableNode(new NodeID("2"), new NodeID("0"));
        assertTrue(rootNode.add(childNode2));

        TreeNode childNode3 = buildNode(new NodeID("3"), new NodeID("1"));
        assertTrue(rootNode.add(childNode3));

        TreeNode childNode4 = buildNode(new NodeID("4"), new NodeID("2"));
        assertThrows(NodeNotSupportException.class, () -> rootNode.add(childNode4));
    }

    @Test
    public void should_exist_child_node_after_add(){
        TreeNode rootNode = buildNode(new NodeID("0"), null);
        TreeNode childNode1 = buildNode(new NodeID("1"), new NodeID("0"));
        childNode1.setChildNodeList(Arrays.asList(
                buildNode(new NodeID("3"), new NodeID("1")),
                buildNode(new NodeID("4"), new NodeID("1"))
        ));

        rootNode.add(childNode1);
        assertTrue(rootNode.exist(buildNode(new NodeID("1"), new NodeID("0"))));
        assertTrue(rootNode.exist(buildNode(new NodeID("3"), new NodeID("1"))));
        assertFalse(rootNode.exist(buildNode(new NodeID("5"), new NodeID("1"))));
    }

    @Test
    public void should_remove_child_node_if_exist(){
        TreeNode rootNode = buildTree();

        assertFalse(rootNode.remove(buildNode(new NodeID("12"), new NodeID("0"))));

        assertTrue(rootNode.exist(buildNode(new NodeID("6"), new NodeID("5"))));
        assertTrue(rootNode.remove(buildNode(new NodeID("6"), new NodeID("5"))));
        assertFalse(rootNode.exist(buildNode(new NodeID("6"), new NodeID("5"))));
        assertFalse(rootNode.remove(buildNode(new NodeID("6"), new NodeID("5"))));


        TreeNode rootNode2 = buildNode(new NodeID("999"), null);
        assertThrows(IllegalNodeException.class, () -> rootNode.remove(rootNode2));

        TreeNode treeNode1 = buildNode(new NodeID("1"), new NodeID("2"));
        TreeNode treeNode2 = buildNode(new NodeID("2"), new NodeID("0"));
        assertThrows(NodeNotSupportException.class, () -> treeNode2.remove(treeNode1));
    }

    @Test
    public void should_rename_right_child_node() {
        TreeNode rootNode = buildTree();
        TreeNode treeNode12 = buildNode(new NodeID("12"), new NodeID("3"));

        assertFalse(rootNode.exist(treeNode12));
        rootNode.add(treeNode12);
        assertTrue(rootNode.exist(treeNode12));

        rootNode.rename(new NodeID("12"), "1222");
        assertTrue(rootNode.exist(treeNode12)); // id相同
        assertTrue(rootNode.exist(buildNameNode(new NodeID("122"), new NodeID("3"), "1222"))); // 名称、类型、父文件相同

        assertThrows(IllegalNodeException.class, () -> rootNode.rename(new NodeID("13"), "should_throws"));
    }

    private TreeNode buildUnExpandableNode(NodeID id, NodeID parentId){
        TreeNode treeNode = buildNode(id, parentId);
        treeNode.setExpandable(Boolean.FALSE);
        return treeNode;
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
        treeNode.setExpandable(true);
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

    /**                     0
     *                 /         \
     *               /            \
     *            1  2  3     4        5
     *                 /    /  \     /   \
     *               11   9   10    6  7  8
     */
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