package indi.melon.ssc.directory.domain.tree;

import indi.melon.ssc.directory.domain.tree.exception.AlreadyExistException;
import indi.melon.ssc.directory.domain.tree.exception.IllegalNodeException;
import indi.melon.ssc.directory.domain.tree.exception.NotFoundException;
import indi.melon.ssc.directory.domain.tree.exception.NotSupportException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Random;

import static indi.melon.ssc.directory.domain.tree.Sort.Order.asc;
import static indi.melon.ssc.directory.domain.tree.Sort.Order.desc;
import static indi.melon.ssc.directory.domain.tree.Sort.TreeNodeField.createTime;
import static indi.melon.ssc.directory.domain.tree.Sort.TreeNodeField.name;
import static indi.melon.ssc.directory.domain.tree.Sort.orderBy;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vvnn1
 * @since 2024/9/22 22:18
 */
class TreeNodeTest {
    @Test
    public void should_add_child_node_normally() {
        TreeNode rootNode = buildDirectoryNode(new NodeID("1"), true);
        TreeNode node2 = buildDirectoryNode(new NodeID("2"));

        rootNode.add(node2);
        assertTrue(rootNode.getChildNodeList().contains(node2));
        assertSame(node2.getParentNode(), rootNode);

        assertDoesNotThrow(() -> rootNode.add(node2));

        TreeNode node2Copy = buildDirectoryNode(new NodeID("2"));
        assertThrows(AlreadyExistException.class, () -> rootNode.add(node2Copy));

        TreeNode node4 = buildDirectoryNode(new NodeID("4"));
        node2.add(node4);
        TreeNode node5 = buildDirectoryNode(new NodeID("5"));
        node4.add(node5);

        TreeNode node6 = buildFileNode(new NodeID("6"));
        node5.add(node6);

        assertThrows(NotSupportException.class, () -> node2.add(node6));

        TreeNode node3 = buildDirectoryNode(new NodeID("3"), true);
        assertThrows(IllegalNodeException.class, () -> rootNode.add(node3));

        TreeNode node7 = buildFileNode(new NodeID("7"));
        node4.locked();
        assertThrows(NotSupportException.class, () -> node5.add(node7));
        node4.unlocked();
    }

    @Test
    public void should_all_be_sorted_with_order() {
        TreeNode rootNode = buildDirectoryNode(new NodeID("0"), true);
        rootNode.sortBy(orderBy(createTime, asc));

        assertNull(rootNode.getChildNodeList());

        for (int i = 1; i < 7; i++) {
            TreeNode treeNode = buildTimeNode(new NodeID(String.valueOf(i)), LocalDateTime.of(2024, new Random().nextInt(11) + 1, new Random().nextInt(29) + 1, new Random().nextInt(23), new Random().nextInt(59)), false);
            rootNode.add(treeNode);
        }
        assertTrue(isCreateTimeAsc(rootNode));


        TreeNode treeNode8 = buildTimeNode(new NodeID("8"), LocalDateTime.of(2024, new Random().nextInt(11) + 1, new Random().nextInt(29) + 1, new Random().nextInt(23), new Random().nextInt(59)), false);
        rootNode.add(treeNode8);
        treeNode8.sortBy(orderBy(name, desc).and(createTime, asc));

        for (int i = 0; i < 10; i++) {
            TreeNode treeNode = buildTimeNode(new NodeID(String.valueOf(i+10)), LocalDateTime.of(2024, new Random().nextInt(11) + 1, new Random().nextInt(29) + 1, new Random().nextInt(23), new Random().nextInt(59)), (i&1) == 1);
            treeNode.setName("treeNode" + i/2);
            treeNode8.add(treeNode);
        }

        assertTrue(isNameDescAndCreateTimeAsc(treeNode8));

        TreeNode node10 = buildFileNode(new NodeID("10"));
        assertThrows(NotSupportException.class, () -> node10.sortBy(orderBy(name, desc).and(createTime, asc)));
    }

    @Test
    public void should_be_parent_if_it_is_expandable(){
        TreeNode treeNode = buildDirectoryNode(new NodeID("1"), true);
        assertTrue(treeNode.getExpandable());
        assertTrue(treeNode.add(buildFileNode(new NodeID("2"))));

        treeNode.setExpandable(false);
        assertThrows(NotSupportException.class, () -> treeNode.add(buildFileNode(new NodeID("3"))));
    }

    @Test
    public void should_remove_child_node_if_exist(){
        TreeNode rootNode = buildDirectoryNode(new NodeID("1"), true);
        TreeNode node2 = buildDirectoryNode(new NodeID("2"));
        assertFalse(rootNode.remove(node2));

        rootNode.locked();
        assertThrows(NotSupportException.class, () -> rootNode.remove(node2));
        rootNode.unlocked();

        assertTrue(rootNode.add(node2));
        assertTrue(rootNode.remove(node2));
        assertFalse(rootNode.getChildNodeList().contains(node2));
    }

    @Test
    public void should_lock_and_unlock_normally() {
        TreeNode treeNode = buildDirectoryNode(new NodeID("0"), true);
        assertFalse(treeNode.isLocked());

        treeNode.locked();
        assertTrue(treeNode.isLocked());

        treeNode.unlocked();
        assertFalse(treeNode.isLocked());
    }

    @Test
    public void should_rename_normally() {
        TreeNode rootNode = buildDirectoryNode(new NodeID("1"), true);
        TreeNode node2 = buildDirectoryNode(new NodeID("2"));
        rootNode.add(node2);

        rootNode.rename(new NodeID("2"), "testName");
        assertEquals("testName", rootNode.get(new NodeID("2")).getName());

        assertThrows(NotFoundException.class, () -> rootNode.rename(new NodeID("3"), "testName"));

        TreeNode node3 = buildDirectoryNode(new NodeID("3"));
        rootNode.add(node3);
        node3.locked();
        assertThrows(NotSupportException.class, () -> rootNode.rename(new NodeID("3"), "testName3"));

        TreeNode node4 = buildDirectoryNode(new NodeID("4"));
        rootNode.add(node4);
        String name4 = node4.getName();
        assertThrows(AlreadyExistException.class, () -> rootNode.rename(new NodeID("4"), "testName"));
        assertEquals(name4, rootNode.get(new NodeID("4")).getName());

        TreeNode node5 = buildFileNode(new NodeID("5"));
        rootNode.add(node5);
        assertDoesNotThrow(() -> rootNode.rename(new NodeID("5"), "testName"));
        assertEquals("testName", rootNode.get(new NodeID("5")).getName());

    }


    @Test
    public void should_move_to_right_parent(){
        TreeNode rootNode = buildDirectoryNode(new NodeID("0"), true);
        TreeNode node2 = buildDirectoryNode(new NodeID("2"));

        assertThrows(NotSupportException.class, () -> rootNode.moveTo(node2));

        assertThrows(NotSupportException.class, () -> node2.moveTo(rootNode));

        rootNode.locked();
        assertThrows(NotSupportException.class, () -> node2.moveTo(rootNode));
        rootNode.unlocked();

        TreeNode node3 = buildFileNode(new NodeID("3"));
        rootNode.add(node3);

        assertThrows(NotSupportException.class, () -> node2.moveTo(node3));

        assertTrue(rootNode.add(node2));
        assertTrue(node3.moveTo(node2));
        assertSame(node3.getParentNode(), node2);
        assertTrue(node2.getChildNodeList().contains(node3));
    }

    @Test
    public void should_get_child_normally() {
        TreeNode rootNode = buildDirectoryNode(new NodeID("0"), true);
        TreeNode node1 = buildDirectoryNode(new NodeID("1"));
        TreeNode node2 = buildFileNode(new NodeID("2"));
        rootNode.add(node1);
        rootNode.add(node2);

        assertNull(rootNode.get(new NodeID("3")));
        assertSame(node2, rootNode.get(new NodeID("2")));

        TreeNode node3 = buildFileNode(new NodeID("3"));
        node1.add(node3);

        assertSame(node3, rootNode.get(new NodeID("3")));
    }

    @Test
    public void should_conflict_normally() {
        TreeNode rootNode = buildDirectoryNode(new NodeID("0"), true);

        TreeNode node2 = buildDirectoryNode(new NodeID("2"));
        assertFalse(rootNode.checkNodeConflict(node2));
        rootNode.add(node2);

        TreeNode node3 = buildDirectoryNode(new NodeID("3"));
        assertFalse(rootNode.checkNodeConflict(node3));
        rootNode.add(node3);
        assertFalse(rootNode.checkNodeConflict(node3));

        TreeNode node4 = buildFileNode(new NodeID("4"));
        node4.setName(node3.getName());
        assertFalse(rootNode.checkNodeConflict(node4));
        rootNode.add(node4);

        TreeNode node5 = buildDirectoryNode(new NodeID("5"));
        node5.setName(node3.getName());
        assertTrue(rootNode.checkNodeConflict(node5));
    }

    private boolean isCreateTimeAsc(TreeNode node) {
        if (node.getChildNodeList() == null) {
            return true;
        }

        TreeNode preNode = null;

        for (TreeNode treeNode : node.getChildNodeList()) {
            if (preNode != null) {
                if (preNode.getCreateTime().isAfter(treeNode.getCreateTime())) {
                    return false;
                }
            }
            preNode = treeNode;
        }

        return true;
    }

    private TreeNode buildTimeNode(NodeID id, LocalDateTime localDateTime, boolean isFileNode) {
        TreeNode treeNode =isFileNode ? buildFileNode(id) : buildDirectoryNode(id);
        treeNode.setCreateTime(localDateTime);
        return treeNode;
    }


    private boolean isNameDescAndCreateTimeAsc(TreeNode node) {
        TreeNode preNode = null;
        for (TreeNode treeNode : node.getChildNodeList()) {
            if (preNode != null) {
                if (preNode.getName().compareTo(treeNode.getName()) < 0) {
                    return false;
                }

                if (preNode.getName().compareTo(treeNode.getName()) == 0) {
                    if (preNode.getCreateTime().isAfter(treeNode.getCreateTime())) {
                        return false;
                    }
                }
            }
            preNode = treeNode;
        }
        return true;
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