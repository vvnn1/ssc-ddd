package indi.melon.ssc.directory.context.domain.tree;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static indi.melon.ssc.directory.context.domain.tree.Order.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author wangmenglong
 * @since 2024/9/26 19:11
 */
class OrderTest {

    @Test
    public void should_generate_right_order() {
        Order nameAscAndTypeDesc = orderBy(name, asc).and(type, desc);

        TreeNode rootNode = buildNode(new NodeID("0"), null, "root", "1", LocalDateTime.of(2023, 1, 1, 1, 1));

        TreeNode treeNode7 = buildNode(new NodeID("6"), new NodeID("0"), "node7", "1", LocalDateTime.of(2023, 1, 1, 7, 1));
        rootNode.setChildNodeList(
                Arrays.asList(
                        treeNode7,
                        buildNode(new NodeID("6"), new NodeID("0"), "node5", "1", LocalDateTime.of(2023, 1, 1, 6, 1)),
                        buildNode(new NodeID("4"), new NodeID("0"), "node4", "1", LocalDateTime.of(2023, 1, 1, 4, 1)),
                        buildNode(new NodeID("2"), new NodeID("0"), "node2", "1", LocalDateTime.of(2023, 1, 1, 4, 1)),
                        buildNode(new NodeID("5"), new NodeID("0"), "node2", "2", LocalDateTime.of(2023, 1, 1, 3, 1)),
                        buildNode(new NodeID("1"), new NodeID("0"), "node1", "1", LocalDateTime.of(2023, 1, 1, 2, 1)),
                        buildNode(new NodeID("3"), new NodeID("0"), "node", "2", LocalDateTime.of(2023, 1, 1, 1, 1))
                )
        );


        rootNode.setOrder(nameAscAndTypeDesc);

        assertArrayEquals(rootNode.getChildNodeList().stream().map(TreeNode::getId).toArray(), new Object[]{
                buildNode(new NodeID("3"), new NodeID("0"), "node", "2", LocalDateTime.of(2023, 1, 1, 1, 1)).getId(),
                buildNode(new NodeID("1"), new NodeID("0"), "node1", "1", LocalDateTime.of(2023, 1, 1, 2, 1)).getId(),
                buildNode(new NodeID("5"), new NodeID("0"), "node2", "2", LocalDateTime.of(2023, 1, 1, 3, 1)).getId(),
                buildNode(new NodeID("2"), new NodeID("0"), "node2", "1", LocalDateTime.of(2023, 1, 1, 4, 1)).getId(),
                buildNode(new NodeID("4"), new NodeID("0"), "node4", "1", LocalDateTime.of(2023, 1, 1, 4, 1)).getId(),
                buildNode(new NodeID("6"), new NodeID("0"), "node5", "1", LocalDateTime.of(2023, 1, 1, 6, 1)).getId(),
                treeNode7.getId(),
        });

        Order createTimeDescAndNameAsc = orderBy(createTime, desc).and(name, asc);
        rootNode.setOrder(createTimeDescAndNameAsc);

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

    private TreeNode buildNode(NodeID id, NodeID parentId, String name, String type, LocalDateTime localDateTime) {
        TreeNode treeNode = new TreeNode();
        treeNode.setId(id);
        treeNode.setName(name);
        treeNode.setParentId(parentId);
        treeNode.setExpandable(true);
        treeNode.setChildNodeList(new ArrayList<>());
        treeNode.setCreateTime(localDateTime);
        treeNode.setType(type);
        return treeNode;
    }
}