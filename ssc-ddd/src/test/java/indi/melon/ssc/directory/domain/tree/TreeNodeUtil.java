package indi.melon.ssc.directory.domain.tree;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author vvnn1
 * @since 2024/10/12 22:36
 */
public class TreeNodeUtil {
    public static TreeNode buildNode(NodeID id, NodeID parentId){
        return new TreeNode(
                id,
                "treeNode-" + id,
                "directory",
                new ArrayList<>(),
                parentId,
                true,
                false
        );
    }

    public static TreeNode buildUnExpandableNode(NodeID id, NodeID parentId){
        TreeNode treeNode = buildNode(id, parentId);
        treeNode.setExpandable(false);
        return treeNode;
    }

    public static TreeNode buildTree() {
        TreeNode rootNode = buildNode(new NodeID("0"), null);
        Arrays.asList(
                buildNode(new NodeID("1"), new NodeID("0")),
                buildNode(new NodeID("2"), new NodeID("0")),
                buildNode(new NodeID("3"), new NodeID("0")),
                buildNode(new NodeID("4"), new NodeID("3")),
                buildNode(new NodeID("5"), new NodeID("3")),
                buildUnExpandableNode(new NodeID("6"), new NodeID("5"))
        ).forEach(rootNode::add);
        return rootNode;
    }
}
