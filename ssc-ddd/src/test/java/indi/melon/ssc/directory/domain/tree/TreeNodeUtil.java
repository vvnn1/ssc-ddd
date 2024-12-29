package indi.melon.ssc.directory.domain.tree;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author vvnn1
 * @since 2024/10/12 22:36
 */
public class TreeNodeUtil {
    public static TreeNode buildNode(NodeID id){
        return new TreeNode(
                id,
                "treeNode-" + id,
                "directory",
                true,
                false
        );
    }

    public static TreeNode buildRootNode(NodeID id){
        return new TreeNode(
                id,
                "treeNode-" + id,
                "directory",
                true,
                true
        );
    }

    public static TreeNode buildUnExpandableNode(NodeID id){
        TreeNode treeNode = buildNode(id);
        treeNode.setExpandable(false);
        return treeNode;
    }

    public static TreeNode buildTree() {
        TreeNode rootNode = buildRootNode(new NodeID("0"));

        TreeNode node1 = buildNode(new NodeID("1"));
        TreeNode node2 = buildNode(new NodeID("1"));
        TreeNode node3 = buildNode(new NodeID("1"));
        TreeNode node4 = buildNode(new NodeID("1"));
        TreeNode node5 = buildNode(new NodeID("1"));
        TreeNode node6 = buildUnExpandableNode(new NodeID("6"));

        rootNode.add(node1);
        rootNode.add(node2);
        rootNode.add(node3);

        node3.add(node4);
        node3.add(node5);

        node5.add(node6);
        return rootNode;
    }
}
