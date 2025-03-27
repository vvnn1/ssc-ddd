package indi.melon.ssc.directory.domain.tree;

import indi.melon.ssc.directory.domain.south.factory.TreeNodeFactory;
import indi.melon.ssc.directory.domain.south.repository.TreeNodeRepository;
import indi.melon.ssc.directory.domain.tree.exception.NotFoundException;
import indi.melon.ssc.directory.domain.tree.exception.NotSupportException;
import lombok.AllArgsConstructor;

/**
 * @author wangmenglong
 * @since 2025/1/13 19:29
 */
@AllArgsConstructor
public class TreeNodeManager {
    private final TreeNodeRepository treeNodeRepository;
    private final TreeNodeFactory treeNodeFactory;

    public TreeNode createRootTreeNode(String name, String type, boolean expandable) {
        TreeNode rootNode = treeNodeFactory.create(
                name,
                type,
                expandable,
                true
        );
        return treeNodeRepository.save(rootNode);
    }

    public TreeNode createTreeNode(String name, String type, boolean expandable, NodeID parentId) {
        TreeNode tempNode = treeNodeFactory.create(
                name,
                type,
                expandable,
                false
        );

        TreeNode parentNode = treeNodeRepository.treeNodeOf(parentId);

        if (parentNode == null) {
            throw new NotFoundException("parent node " + parentId + " not exists.");
        }

        parentNode.add(tempNode);
        treeNodeRepository.save(parentNode);
        return parentNode.get(tempNode.getId());
    }
}
