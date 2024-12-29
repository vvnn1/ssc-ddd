package indi.melon.ssc.directory.north.local.appservice;

import indi.melon.ssc.common.exception.ApplicationDomainException;
import indi.melon.ssc.directory.domain.tree.TreeNode;
import indi.melon.ssc.directory.domain.tree.NodeID;
import indi.melon.ssc.directory.domain.tree.TreeNodeManager;
import indi.melon.ssc.directory.north.local.message.CreateNodeCommand;
import indi.melon.ssc.directory.north.local.message.DropNodeCommand;
import indi.melon.ssc.directory.north.local.message.MoveNodeCommand;
import indi.melon.ssc.directory.north.local.message.RenameNodeCommand;
import indi.melon.ssc.domain.common.cqrs.DomainException;
import org.springframework.stereotype.Service;

/**
 * @author vvnn1
 * @since 2024/10/5 12:39
 */
@Service
public class TreeNodeAppService {
    private final TreeNodeManager treeNodeManager;

    public TreeNodeAppService(TreeNodeManager treeNodeManager) {
        this.treeNodeManager = treeNodeManager;
    }

    /**
     * 新建节点
     *
     * @param createCommand 新建节点命令
     * @return 所建节点id
     */
    public String create(CreateNodeCommand createCommand) {
        if (createCommand.isCreateRootNode()) {
            TreeNode rootNode = treeNodeManager.createRootTreeNode(
                    createCommand.name(),
                    createCommand.type(),
                    createCommand.expandable()
            );

            return rootNode.getId().getValue();
        }

        TreeNode childNode = treeNodeManager.createTreeNode(
                createCommand.name(),
                createCommand.type(),
                createCommand.expandable(),
                new NodeID(createCommand.parentId())
        );
        return childNode.getId().getValue();
    }

    /**
     * 重命名节点
     *
     * @param renameCommand 重命名命令
     */
    public void rename(RenameNodeCommand renameCommand) {
        try {
            treeNodeManager.renameTreeNode(
                    new NodeID(renameCommand.id()),
                    renameCommand.newName()
            );
        } catch (DomainException e) {
            throw new ApplicationDomainException("node rename fail.", e);
        }
    }

    /**
     * 删除节点
     *
     * @param dropNodeCommand 删除命令
     */
    public void drop(DropNodeCommand dropNodeCommand) {
        try {
            treeNodeManager.removeTreeNode(
                    new NodeID(dropNodeCommand.id())
            );
        } catch (DomainException e) {
            throw new ApplicationDomainException("node drop fail.", e);
        }

    }

    /**
     * 移动节点
     *
     * @param moveNodeCommand 移动命令
     */
    public void move(MoveNodeCommand moveNodeCommand) {
        try {
            treeNodeManager.moveTreeNode(
                    new NodeID(moveNodeCommand.nodeId()),
                    new NodeID(moveNodeCommand.parentId())
            );
        } catch (DomainException e) {
            throw new ApplicationDomainException("move node " + moveNodeCommand.nodeId() + " to " + moveNodeCommand.parentId() + " failed.", e);
        }
    }
}
