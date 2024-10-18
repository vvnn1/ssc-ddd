package indi.melon.ssc.directory.north.local.appservice;

import indi.melon.ssc.common.exception.ApplicationDomainException;
import indi.melon.ssc.common.exception.ApplicationValidationException;
import indi.melon.ssc.directory.domain.tree.TreeNode;
import indi.melon.ssc.directory.domain.tree.NodeID;
import indi.melon.ssc.directory.domain.south.factory.TreeNodeFactory;
import indi.melon.ssc.directory.domain.south.repository.TreeNodeRepository;
import indi.melon.ssc.directory.north.local.message.CreateNodeCommand;
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
            return rootNode.getId().getId();
        }

        TreeNode rootNode = treeNodeRepository.treeNodeOf(
                new NodeID(
                        createCommand.rootNodeId()
                )
        );

        if (rootNode == null) {
            throw new ApplicationValidationException("can not found root node by id " + createCommand.rootNodeId() + ".");
        }

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
        return treeNode.getId().getId();
    }
}
