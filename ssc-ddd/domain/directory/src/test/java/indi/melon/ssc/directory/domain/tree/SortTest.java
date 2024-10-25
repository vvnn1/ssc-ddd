package indi.melon.ssc.directory.domain.tree;

import indi.melon.ssc.directory.domain.tree.exception.IllegalSortException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author wangmenglong
 * @since 2024/9/26 19:11
 */
class SortTest {

    @Test
    public void should_generate_right_order() {
        Sort nameAscAndTypeDesc = Sort.orderBy(Sort.TreeNodeField.name, Sort.Order.asc).and(Sort.TreeNodeField.type, Sort.Order.desc);

        TreeNode rootNode = buildNode(new NodeID("0"), null, "root", "1", LocalDateTime.of(2023, 1, 1, 1, 1));

        TreeNode treeNode7 = buildNode(new NodeID("7"), new NodeID("0"), "node7", "1", LocalDateTime.of(2023, 1, 1, 7, 1));
        Arrays.asList(
                treeNode7,
                buildNode(new NodeID("6"), new NodeID("0"), "node5", "1", LocalDateTime.of(2023, 1, 1, 6, 1)),
                buildNode(new NodeID("4"), new NodeID("0"), "node4", "1", LocalDateTime.of(2023, 1, 1, 4, 1)),
                buildNode(new NodeID("2"), new NodeID("0"), "node2", "1", LocalDateTime.of(2023, 1, 1, 4, 1)),
                buildNode(new NodeID("5"), new NodeID("0"), "node2", "2", LocalDateTime.of(2023, 1, 1, 3, 1)),
                buildNode(new NodeID("1"), new NodeID("0"), "node1", "1", LocalDateTime.of(2023, 1, 1, 2, 1)),
                buildNode(new NodeID("3"), new NodeID("0"), "node", "2", LocalDateTime.of(2023, 1, 1, 1, 1))
        ).forEach(rootNode::add);

        rootNode.sortBy(nameAscAndTypeDesc);

        assertArrayEquals(rootNode.getChildNodeList().stream().map(TreeNode::getId).toArray(), new Object[]{
                buildNode(new NodeID("3"), new NodeID("0"), "node", "2", LocalDateTime.of(2023, 1, 1, 1, 1)).getId(),
                buildNode(new NodeID("1"), new NodeID("0"), "node1", "1", LocalDateTime.of(2023, 1, 1, 2, 1)).getId(),
                buildNode(new NodeID("5"), new NodeID("0"), "node2", "2", LocalDateTime.of(2023, 1, 1, 3, 1)).getId(),
                buildNode(new NodeID("2"), new NodeID("0"), "node2", "1", LocalDateTime.of(2023, 1, 1, 4, 1)).getId(),
                buildNode(new NodeID("4"), new NodeID("0"), "node4", "1", LocalDateTime.of(2023, 1, 1, 4, 1)).getId(),
                buildNode(new NodeID("6"), new NodeID("0"), "node5", "1", LocalDateTime.of(2023, 1, 1, 6, 1)).getId(),
                treeNode7.getId(),
        });

        Sort createTimeDescAndNameAsc = Sort.orderBy(Sort.TreeNodeField.createTime, Sort.Order.desc).and(Sort.TreeNodeField.name, Sort.Order.asc);
        rootNode.sortBy(createTimeDescAndNameAsc);

        assertArrayEquals(rootNode.getChildNodeList().stream().map(TreeNode::getId).toArray(), new Object[]{
                treeNode7.getId(),
                buildNode(new NodeID("6"), new NodeID("0"), "node5", "1", LocalDateTime.of(2023, 1, 1, 6, 1)).getId(),
                buildNode(new NodeID("2"), new NodeID("0"), "node2", "1", LocalDateTime.of(2023, 1, 1, 4, 1)).getId(),
                buildNode(new NodeID("4"), new NodeID("0"), "node4", "1", LocalDateTime.of(2023, 1, 1, 4, 1)).getId(),
                buildNode(new NodeID("5"), new NodeID("0"), "node2", "2", LocalDateTime.of(2023, 1, 1, 3, 1)).getId(),
                buildNode(new NodeID("1"), new NodeID("0"), "node1", "1", LocalDateTime.of(2023, 1, 1, 2, 1)).getId(),
                buildNode(new NodeID("3"), new NodeID("0"), "node", "2", LocalDateTime.of(2023, 1, 1, 1, 1)).getId(),
        });
    }

    @Test
    public void should_serialize_and_deserialize_normally() {
        String sortStr = "nameAsc.typeDesc.createTimeDesc";
        Sort sort = Sort.orderBy(Sort.TreeNodeField.name, Sort.Order.asc).and(Sort.TreeNodeField.type, Sort.Order.desc).and(Sort.TreeNodeField.createTime, Sort.Order.desc);
        assertEquals(sortStr, sort.toString());

        assertThrows(IllegalSortException.class, () -> Sort.deserialize("kkAsc.typeDesc"));
        assertThrows(IllegalSortException.class, () -> Sort.deserialize("nameAsc11.typeDesc"));
        assertThrows(IllegalSortException.class, () -> Sort.deserialize("nameAsc11typeDesc"));


        TreeNode rootNode = buildNode(new NodeID("0"), null, "root", "1", LocalDateTime.of(2023, 1, 1, 1, 1));

        TreeNode treeNode7 = buildNode(new NodeID("7"), new NodeID("0"), "node7", "1", LocalDateTime.of(2023, 1, 1, 7, 1));
        Arrays.asList(
                treeNode7,
                buildNode(new NodeID("6"), new NodeID("0"), "node5", "1", LocalDateTime.of(2023, 1, 1, 6, 1)),
                buildNode(new NodeID("4"), new NodeID("0"), "node4", "1", LocalDateTime.of(2023, 1, 1, 4, 1)),
                buildNode(new NodeID("2"), new NodeID("0"), "node2", "1", LocalDateTime.of(2023, 1, 1, 4, 1)),
                buildNode(new NodeID("5"), new NodeID("0"), "node2", "2", LocalDateTime.of(2023, 1, 1, 3, 1)),
                buildNode(new NodeID("1"), new NodeID("0"), "node1", "1", LocalDateTime.of(2023, 1, 1, 2, 1)),
                buildNode(new NodeID("3"), new NodeID("0"), "node", "2", LocalDateTime.of(2023, 1, 1, 1, 1))
        ).forEach(rootNode::add);

        sort = Sort.deserialize("typeDesc.createTimeAsc");
        rootNode.sortBy(sort);

        assertArrayEquals(rootNode.getChildNodeList().stream().map(TreeNode::getId).toArray(), new Object[]{
                buildNode(new NodeID("3"), new NodeID("0"), "node", "2", LocalDateTime.of(2023, 1, 1, 1, 1)).getId(),
                buildNode(new NodeID("5"), new NodeID("0"), "node2", "2", LocalDateTime.of(2023, 1, 1, 3, 1)).getId(),
                buildNode(new NodeID("1"), new NodeID("0"), "node1", "1", LocalDateTime.of(2023, 1, 1, 2, 1)).getId(),
                buildNode(new NodeID("4"), new NodeID("0"), "node4", "1", LocalDateTime.of(2023, 1, 1, 3, 1)).getId(),
                buildNode(new NodeID("2"), new NodeID("0"), "node2", "1", LocalDateTime.of(2023, 1, 1, 4, 1)).getId(),
                buildNode(new NodeID("6"), new NodeID("0"), "node5", "1", LocalDateTime.of(2023, 1, 1, 6, 1)).getId(),
                treeNode7.getId(),
        });

    }

    private TreeNode buildNode(NodeID id, NodeID parentId, String name, String type, LocalDateTime createTime) {
        TreeNode treeNode = new TreeNode();
        treeNode.setId(id);
        treeNode.setName(name);
        treeNode.setParentId(parentId);
        treeNode.setExpandable(true);
        treeNode.setCreateTime(createTime);
        treeNode.setType(type);
        return treeNode;
    }
}