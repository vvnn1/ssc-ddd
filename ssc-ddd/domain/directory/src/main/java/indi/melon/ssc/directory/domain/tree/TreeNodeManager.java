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

    public boolean removeTreeNode(NodeID id) {
        TreeNode treeNode = treeNodeRepository.treeNodeOf(id);
        if (treeNode == null) {
            return false;
        }

        if (treeNode.isRootNode()) {
            throw new NotSupportException("node " + id + " is root node. it can be removed");
        }

        TreeNode parentNode = treeNode.getParentNode();
        parentNode.remove(treeNode);

        treeNodeRepository.save(parentNode);
        return true;

    }

    public void moveTreeNode(NodeID id, NodeID parentId) {
        TreeNode treeNode = treeNodeRepository.treeNodeOf(id);
        if (treeNode == null) {
            throw new NotFoundException("node " + id + " not found");
        }

        TreeNode parentNode = treeNodeRepository.treeNodeOf(parentId);
        if (parentNode == null) {
            throw new NotFoundException("parent " + parentId + " not found");
        }

        treeNode.moveTo(parentNode);
        treeNodeRepository.save(treeNode);
    }

    public void renameTreeNode(NodeID id, String newName) {
        TreeNode treeNode = treeNodeRepository.treeNodeOf(id);
        if (treeNode == null) {
            throw new NotFoundException("node " + id + " not found");
        }

        if (treeNode.isRoot()) {
            throw new NotSupportException("node " + id + " is root node. it can not be renamed");
        }

        TreeNode parentNode = treeNode.getParentNode();
        parentNode.rename(treeNode.getId(), newName);
        treeNodeRepository.save(parentNode);
    }
}
