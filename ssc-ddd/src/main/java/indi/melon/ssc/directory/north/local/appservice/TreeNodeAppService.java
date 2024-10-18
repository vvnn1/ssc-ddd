package indi.melon.ssc.directory.north.local.appservice;

import indi.melon.ssc.common.exception.ApplicationDomainException;
import indi.melon.ssc.common.exception.ApplicationValidationException;
import indi.melon.ssc.domain.directory.tree.TreeNode;
import indi.melon.ssc.domain.directory.tree.NodeID;
import indi.melon.ssc.domain.directory.south.factory.TreeNodeFactory;
import indi.melon.ssc.domain.directory.south.repository.TreeNodeRepository;
import indi.melon.ssc.directory.north.local.message.TreeNodeCreateCommand;
import indi.melon.ssc.domain.common.cqrs.DomainException;

/**
 * @author vvnn1
 * @since 2024/10/5 12:39
 */
public class TreeNodeAppService {
    private final TreeNodeRepository treeNodeRepository;
    private final TreeNodeFactory treeNodeFactory;

    public TreeNodeAppService(TreeNodeRepository treeNodeRepository, TreeNodeFactory treeNodeFactory) {
        this.treeNodeRepository = treeNodeRepository;
        this.treeNodeFactory = treeNodeFactory;
    }

    public void create(TreeNodeCreateCommand createCommand) {
        if (createCommand.treeNode() == null){
            throw new ApplicationValidationException("can not create a null node.");
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
            treeNodeRepository.save(rootNode);
        } catch (DomainException e){
            throw new ApplicationDomainException("node create fail.", e);
        }
    }
}
