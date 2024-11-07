package indi.melon.ssc.directory.north.local.appservice;

import indi.melon.ssc.common.exception.ApplicationDomainException;
import indi.melon.ssc.common.exception.ApplicationValidationException;
import indi.melon.ssc.directory.domain.tree.TreeNode;
import indi.melon.ssc.directory.domain.tree.NodeID;
import indi.melon.ssc.directory.domain.south.factory.TreeNodeFactory;
import indi.melon.ssc.directory.domain.south.repository.TreeNodeRepository;
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
    private final TreeNodeRepository treeNodeRepository;
    private final TreeNodeFactory treeNodeFactory;

    public TreeNodeAppService(TreeNodeRepository treeNodeRepository, TreeNodeFactory treeNodeFactory) {
        this.treeNodeRepository = treeNodeRepository;
        this.treeNodeFactory = treeNodeFactory;
    }

    /**
     * 新建节点
     * @param createCommand 新建节点命令
     * @return 所建节点id
     */
    public String create(CreateNodeCommand createCommand) {
        if (createCommand.isCreateRootNode()) {
            TreeNode rootNode = treeNodeFactory.create(
                    createCommand.treeNode().name(),
                    createCommand.treeNode().type(),
                    createCommand.treeNode().expandable(),
                    createCommand.treeNode().parentNodeId()
            );

            treeNodeRepository.save(rootNode);
            return rootNode.getId().getValue();
        }

        TreeNode rootNode = nonNullRootNodeOf(createCommand.rootNodeId());

        TreeNode treeNode = treeNodeFactory.create(
                createCommand.treeNode().name(),
                createCommand.treeNode().type(),
                createCommand.treeNode().expandable(),
                createCommand.treeNode().parentNodeId()
        );

        try {
            rootNode.add(treeNode);
        } catch (DomainException e){
            throw new ApplicationDomainException("node create fail.", e);
        }

        treeNodeRepository.save(rootNode);
        return treeNode.getId().getValue();
    }

    /**
     * 重命名节点
     * @param renameCommand 重命名命令
     */
    public void rename(RenameNodeCommand renameCommand) {
        TreeNode rootNode = nonNullRootNodeOf(renameCommand.rootNodeId());

        try {
            rootNode.rename(
                    new NodeID(renameCommand.treeNode().id()),
                    renameCommand.treeNode().newName()
            );
        }catch (DomainException e){
            throw new ApplicationDomainException("node rename fail.", e);
        }

        treeNodeRepository.save(rootNode);
    }

    /**
     * 删除节点
     * @param dropNodeCommand 删除命令
     */
    public void drop(DropNodeCommand dropNodeCommand) {
        TreeNode rootNode = nonNullRootNodeOf(dropNodeCommand.rootNodeId());

        try {
            rootNode.remove(
                    new NodeID(dropNodeCommand.treeNode().id())
            );
        } catch (DomainException e) {
            throw new ApplicationDomainException("node drop fail.", e);
        }

        treeNodeRepository.save(rootNode);
    }

    /**
     * 移动节点
     * @param moveNodeCommand 移动命令
     */
    public void move(MoveNodeCommand moveNodeCommand) {
        TreeNode rootNode = nonNullRootNodeOf(moveNodeCommand.rootNodeId());

        String treeNodeId = moveNodeCommand.treeNode().id();
        String parentNodeId = moveNodeCommand.treeNode().newParentId();
        try {
            rootNode.move(
                    new NodeID(treeNodeId),
                    new NodeID(parentNodeId)
            );
        } catch (DomainException e) {
            throw new ApplicationDomainException("move node " + treeNodeId + " to " + parentNodeId +" failed.", e);
        }
    }

    private TreeNode nonNullRootNodeOf(String rootNodeId) {
        TreeNode rootNode = treeNodeRepository.treeNodeOf(
                new NodeID(rootNodeId)
        );

        if (rootNode == null) {
            throw new ApplicationValidationException("can not found root node by draftId " + rootNodeId + ".");
        }
        return rootNode;
    }
}
