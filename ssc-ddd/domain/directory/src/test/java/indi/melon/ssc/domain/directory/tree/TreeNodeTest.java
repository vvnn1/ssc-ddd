package indi.melon.ssc.domain.directory.tree;

import indi.melon.ssc.domain.directory.exception.IllegalNodeException;
import indi.melon.ssc.domain.directory.exception.AlreadyExistException;
import indi.melon.ssc.domain.directory.exception.NotFoundException;
import indi.melon.ssc.domain.directory.exception.NotSupportException;
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
    public void should_has_right_parent_node_if_be_added(){
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
    public void should_throw_exception_if_add_same_node_to_same_parent() {
        TreeNode rootNode = buildTree(); // 0-11

        TreeNode childNode12 = buildNode(new NodeID("12"), new NodeID("3"));
        assertTrue(rootNode.add(childNode12));

        TreeNode childNode13 = buildNode(new NodeID("13"), new NodeID("0"));
        assertTrue(rootNode.add(childNode13));

        TreeNode childNode14 = buildNameNode(new NodeID("14"), new NodeID("5"), "Node-14");
        assertTrue(rootNode.add(childNode14));

        TreeNode childNode14Copy = buildNameNode(new NodeID("14-1"), new NodeID("5"), "Node-14");
        assertThrows(AlreadyExistException.class, () -> rootNode.add(childNode14Copy));

        TreeNode childNode14File = buildNameNode(new NodeID("14-2"), new NodeID("5"), "Node-14");
        childNode14File.setType("file");
        assertTrue(rootNode.add(childNode14File));
    }

    @Test
    public void should_all_be_sorted_with_order() {
        TreeNode rootNode = buildNode(new NodeID("0"), null);

        for (int i = 1; i < 7; i++) {
            TreeNode treeNode = buildTimeNode(new NodeID(String.valueOf(i)), new NodeID("0"), LocalDateTime.of(2024, new Random().nextInt(11) + 1, new Random().nextInt(29) + 1, new Random().nextInt(23), new Random().nextInt(59)));
            rootNode.add(treeNode);
        }

        TreeNode treeNode8 = buildTimeNode(new NodeID("8"), new NodeID("0"), LocalDateTime.of(2024, new Random().nextInt(11) + 1, new Random().nextInt(29) + 1, new Random().nextInt(23), new Random().nextInt(59)));
        rootNode.add(treeNode8);

        for (int i = 0; i < 5; i++) {
            TreeNode treeNode = buildTimeNode(new NodeID(String.valueOf(i+10)), new NodeID("8"), LocalDateTime.of(2024, new Random().nextInt(11) + 1, new Random().nextInt(29) + 1, new Random().nextInt(23), new Random().nextInt(59)));
            rootNode.add(treeNode);
        }

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


        assertThrows(NotSupportException.class, () -> {
            treeNode1.setChildNodeList(List.of(buildNode(new NodeID("4"), new NodeID("1"))));
        });
    }

    @Test
    public void should_add_node_if_its_root_node() {
        TreeNode rootNode = buildNode(new NodeID("0"), null);
        TreeNode childNode1 = buildNode(new NodeID("1"), new NodeID("0"));

        assertTrue(rootNode.add(childNode1));

        TreeNode childNode2 = buildNode(new NodeID("2"), new NodeID("1"));
        assertThrows(NotSupportException.class, () -> childNode1.add(childNode2));
    }

    @Test
    public void should_be_parent_node_if_it_is_expandable(){
        TreeNode rootNode = buildNode(new NodeID("0"), null);
        TreeNode childNode1 = buildNode(new NodeID("1"), new NodeID("0"));
        assertTrue(rootNode.add(childNode1));

        TreeNode childNode2 = buildUnExpandableNode(new NodeID("2"), new NodeID("0"));
        assertTrue(rootNode.add(childNode2));

        TreeNode childNode3 = buildNode(new NodeID("3"), new NodeID("1"));
        assertTrue(rootNode.add(childNode3));

        TreeNode childNode4 = buildNode(new NodeID("4"), new NodeID("2"));
        assertThrows(NotSupportException.class, () -> rootNode.add(childNode4));

        assertThrows(NotSupportException.class, () -> rootNode.move(new NodeID("1"), new NodeID("2")));
    }

    @Test
    public void should_exist_child_node_after_add(){
        TreeNode rootNode = buildNode(new NodeID("0"), null);
        TreeNode childNode1 = buildNode(new NodeID("1"), new NodeID("0"));
        rootNode.add(childNode1);
        rootNode.add(buildNode(new NodeID("3"), new NodeID("1")));
        rootNode.add(buildNode(new NodeID("4"), new NodeID("1")));

        assertTrue(rootNode.exist(buildNode(new NodeID("1"), new NodeID("0"))));
        assertTrue(rootNode.exist(buildNode(new NodeID("3"), new NodeID("1"))));
        assertFalse(rootNode.exist(buildNode(new NodeID("5"), new NodeID("1"))));
    }

    @Test
    public void should_remove_child_node_if_exist(){
        TreeNode rootNode = buildTree();

        assertFalse(rootNode.remove(new NodeID("12")));

        assertTrue(rootNode.exist(buildNode(new NodeID("6"), new NodeID("5"))));
        assertTrue(rootNode.remove(new NodeID("6")));
        assertFalse(rootNode.exist(buildNode(new NodeID("6"), new NodeID("5"))));
        assertFalse(rootNode.remove(new NodeID("6")));


        TreeNode rootNode2 = buildNode(new NodeID("999"), null);
        assertFalse(rootNode.remove(rootNode2.getId()));

        TreeNode treeNode1 = buildNode(new NodeID("1"), new NodeID("2"));
        TreeNode treeNode2 = buildNode(new NodeID("2"), new NodeID("0"));
        assertThrows(NotSupportException.class, () -> treeNode2.remove(treeNode1.getId()));
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

    @Test
    public void should_lock_and_unlock_right(){
        TreeNode rootNode = buildNode(new NodeID("0"), null);

        TreeNode treeNode1 = buildNode(new NodeID("1"), new NodeID("0"));
        TreeNode treeNode2 = buildNode(new NodeID("2"), new NodeID("0"));
        TreeNode treeNode3 = buildNode(new NodeID("3"), new NodeID("0"));
        TreeNode treeNode4 = buildNode(new NodeID("4"), new NodeID("0"));

        Arrays.asList(treeNode1, treeNode2, treeNode3, treeNode4).forEach(rootNode::add);


        TreeNode treeNode5 = buildNode(new NodeID("5"), new NodeID("2"));
        TreeNode treeNode6 = buildNode(new NodeID("6"), new NodeID("2"));
        Arrays.asList(treeNode5, treeNode6).forEach(rootNode::add);

        TreeNode treeNode7 = buildNode(new NodeID("7"), new NodeID("5"));
        rootNode.add(treeNode7);

        List<TreeNode> childNodes = Arrays.asList(treeNode1, treeNode2, treeNode3, treeNode4, treeNode5, treeNode6);

        assertFalse(rootNode.isLocked(new NodeID("7")));
        assertFalse(treeNode7.getLocked());
        assertTrue(childNodes.stream().map(TreeNode::getLocked).allMatch(Boolean.FALSE::equals));

        rootNode.locked(new NodeID("7"));
        assertTrue(rootNode.isLocked(new NodeID("7")));
        assertTrue(treeNode7.getLocked());
        assertTrue(childNodes.stream().map(TreeNode::getLocked).allMatch(Boolean.FALSE::equals));

        assertThrows(NotFoundException.class, () -> rootNode.locked(new NodeID("8")));

        rootNode.unlocked(new NodeID("7"));
        assertFalse(rootNode.isLocked(new NodeID("7")));
        assertFalse(treeNode7.getLocked());
        assertTrue(childNodes.stream().map(TreeNode::getLocked).allMatch(Boolean.FALSE::equals));

        assertThrows(NotFoundException.class, () -> rootNode.unlocked(new NodeID("8")));
    }

    @Test
    public void should_move_node_to_right_parent(){
        TreeNode rootNode = buildNode(new NodeID("0"), null);

        TreeNode treeNode1 = buildNode(new NodeID("1"), new NodeID("0"));
        TreeNode treeNode2 = buildNode(new NodeID("2"), new NodeID("0"));
        Arrays.asList(treeNode1, treeNode2).forEach(rootNode::add);

        rootNode.move(new NodeID("1"), new NodeID("2"));
        assertFalse(rootNode.getChildNodeList().contains(treeNode1));
        assertTrue(treeNode2.getChildNodeList().contains(treeNode1));
        assertEquals(new NodeID("2"), treeNode1.getParentId());

        assertThrows(NotFoundException.class, () -> rootNode.move(new NodeID("3"), new NodeID("2")));
        assertThrows(NotFoundException.class, () -> rootNode.move(new NodeID("2"), new NodeID("3")));
    }

    @Test
    public void should_not_rename_and_remove_and_move_if_locked(){
        TreeNode tempNode = new TreeNode();
        TreeNode rootNode = buildTree();

        TreeNode treeNode13 = buildNode(new NodeID("13"), new NodeID("5"));
        rootNode.add(treeNode13);
        rootNode.rename(new NodeID("13"), "test_rename");
        assertEquals("test_rename", treeNode13.getName());
        rootNode.locked(new NodeID("13"));
        assertThrows(NotSupportException.class, () -> rootNode.rename(new NodeID("13"), "test_rename_locked"));
        assertEquals("test_rename", treeNode13.getName());

        assertTrue(rootNode.remove(new NodeID("11")));

        tempNode.setId(new NodeID("11"));
        assertFalse(rootNode.exist(tempNode));

        rootNode.locked(new NodeID("10"));
        assertThrows(NotSupportException.class, () -> rootNode.remove(new NodeID("10")));
        tempNode.setId(new NodeID("10"));
        assertTrue(rootNode.exist(tempNode));

        TreeNode treeNode14 = buildNode(new NodeID("14"), new NodeID("0"));
        rootNode.add(treeNode14);

        assertThrows(NotSupportException.class, () -> rootNode.move(new NodeID("14"), new NodeID("10")));
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
        treeNode.setLocked(false);
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