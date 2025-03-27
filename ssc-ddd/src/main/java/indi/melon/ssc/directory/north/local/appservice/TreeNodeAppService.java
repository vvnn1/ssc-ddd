package indi.melon.ssc.directory.north.local.appservice;

import indi.melon.ssc.common.exception.ApplicationDomainException;
import indi.melon.ssc.common.exception.ApplicationValidationException;
import indi.melon.ssc.directory.domain.south.factory.TreeNodeFactory;
import indi.melon.ssc.directory.domain.south.repository.TreeNodeRepository;
import indi.melon.ssc.directory.domain.tree.TreeNode;
import indi.melon.ssc.directory.domain.tree.NodeID;
import indi.melon.ssc.directory.domain.tree.TreeNodeManager;
import indi.melon.ssc.directory.north.local.message.CreateNodeCommand;
import indi.melon.ssc.directory.north.local.message.DropNodeCommand;
import indi.melon.ssc.directory.north.local.message.MoveNodeCommand;
import indi.melon.ssc.directory.north.local.message.RenameNodeCommand;
import indi.melon.ssc.domain.common.cqrs.DomainException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author vvnn1
 * @since 2024/10/5 12:39
 */
@Service
public class TreeNodeAppService {
    private final TreeNodeManager treeNodeManager;
    private final TreeNodeRepository treeNodeRepository;

    public TreeNodeAppService(TreeNodeRepository treeNodeRepository, TreeNodeFactory treeNodeFactory) {
        this.treeNodeManager = new TreeNodeManager(treeNodeRepository, treeNodeFactory);
        this.treeNodeRepository = treeNodeRepository;
    }

    /**
     * 新建节点
     *
     * @param createCommand 新建节点命令
     * @return 所建节点id
     */
    @Transactional
    public String create(CreateNodeCommand createCommand) {
        try {
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
        } catch (DomainException e) {
            throw new ApplicationDomainException("create node error.", e);
        }
    }

    /**
     * 重命名节点
     *
     * @param renameCommand 重命名命令
     */
    @Transactional
    public void rename(RenameNodeCommand renameCommand) {
        TreeNode treeNode = treeNodeRepository.treeNodeOf(
                new NodeID(renameCommand.id())
        );

        if(treeNode == null) {
            throw new ApplicationValidationException("node " + renameCommand.id() + " not exist");
        }

        try {
            treeNode.rename(renameCommand.newName());
        } catch (DomainException e) {
            throw new ApplicationDomainException("node rename fail.", e);
        }
    }

    /**
     * 删除节点
     *
     * @param dropNodeCommand 删除命令
     */
    @Transactional
    public void drop(DropNodeCommand dropNodeCommand) {
        TreeNode treeNode = treeNodeRepository.treeNodeOf(
                new NodeID(dropNodeCommand.id())
        );
        if (treeNode == null) {
            throw new ApplicationValidationException("node " + dropNodeCommand.id() + " not exist");
        }

        try {
            treeNode.removeFromParent();
        } catch (DomainException e) {
            throw new ApplicationDomainException("node drop fail.", e);
        }
    }

    /**
     * 移动节点
     *
     * @param moveNodeCommand 移动命令
     */
    @Transactional
    public void move(MoveNodeCommand moveNodeCommand) {
        TreeNode treeNode = treeNodeRepository.treeNodeOf(
                new NodeID(moveNodeCommand.id())
        );
        if (treeNode == null) {
            throw new ApplicationValidationException("node " + moveNodeCommand.id() + " not exist");
        }

        TreeNode parentNode = treeNodeRepository.treeNodeOf(
                new NodeID(moveNodeCommand.parentId())
        );
        if (parentNode == null) {
            throw new ApplicationValidationException("new parent node " + moveNodeCommand.parentId() + " not exist");
        }

        try {
            treeNode.moveTo(parentNode);
        } catch (DomainException e) {
            throw new ApplicationDomainException("move node " + moveNodeCommand.id() + " to " + moveNodeCommand.parentId() + " failed.", e);
        }
    }
}
