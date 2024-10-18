package indi.melon.ssc.directory.north.local.appservice;

import indi.melon.ssc.SscBaseTest;
import indi.melon.ssc.common.exception.ApplicationDomainException;
import indi.melon.ssc.common.exception.ApplicationValidationException;
import indi.melon.ssc.directory.common.BizTagProperties;
import indi.melon.ssc.directory.domain.south.repository.TreeNodeRepository;
import indi.melon.ssc.directory.domain.tree.NodeID;
import indi.melon.ssc.directory.domain.tree.TreeNode;
import indi.melon.ssc.directory.north.local.message.CreateNodeCommand;
import indi.melon.ssc.directory.north.local.message.DropNodeCommand;
import indi.melon.ssc.directory.north.local.message.RenameNodeCommand;
import indi.melon.ssc.ticket.domain.south.repository.TicketBoxRepository;
import indi.melon.ssc.ticket.domain.ticket.BoxID;
import indi.melon.ssc.ticket.domain.ticket.UuidTicketBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author wangmenglong
 * @since 2024/10/17 19:26
 */
class TreeNodeAppServiceIT extends SscBaseTest {

    @Autowired
    private TreeNodeAppService treeNodeAppService;
    @Autowired
    private TreeNodeRepository treeNodeRepository;
    @Autowired
    private TicketBoxRepository ticketBoxRepository;

    @BeforeEach
    void setUp() {
        UuidTicketBox ticketBox = new UuidTicketBox(
                new BoxID(BizTagProperties.TREE_NODE_BIZ_TAG),
                10,
                "test tree node box."
        );
        ticketBoxRepository.save(ticketBox);
    }

    @Test
    void should_create_root_node_normally() {
        assertThrows(ApplicationValidationException.class, () -> new CreateNodeCommand(
                "111", //should not have rootNodeId
                new CreateNodeCommand.TreeNode(
                        "iamroot",
                        "directory",
                        true,
                        null
                )
        ));

        assertThrows(ApplicationValidationException.class, () -> new CreateNodeCommand(
                null,
                new CreateNodeCommand.TreeNode(
                        "iamroot",
                        "directory",
                        true,
                        "111" //child node should assign rootNodeId
                )
        ));

        assertThrows(ApplicationValidationException.class, () -> treeNodeAppService.create(
                new CreateNodeCommand(
                        "111",// not found rootNode
                        new CreateNodeCommand.TreeNode(
                                "iamroot",
                                "directory",
                                true,
                                "111"
                        )
                )
        ));




        CreateNodeCommand rootNodeCreateCommand = new CreateNodeCommand(
                null,
                new CreateNodeCommand.TreeNode(
                        "test_root",
                        "directory",
                        true,
                        null
                )
        );

        String rootNodeId = treeNodeAppService.create(rootNodeCreateCommand);
        assertNotNull(rootNodeId);

        TreeNode rootNode = treeNodeRepository.treeNodeOf(
                new NodeID(rootNodeId)
        );
        assertNotNull(rootNode);
        assertEquals(new NodeID(rootNodeId), rootNode.getId());
        assertEquals(rootNodeCreateCommand.treeNode().name(), rootNode.getName());
        assertEquals(rootNodeCreateCommand.treeNode().type(), rootNode.getType());
        assertEquals(rootNodeCreateCommand.treeNode().expandable(), rootNode.getExpandable());
        assertTrue(rootNode.getChildNodeList().isEmpty());
        assertNull(rootNode.getParentId());

        CreateNodeCommand childNodeCreateCommand = new CreateNodeCommand(
                rootNodeId,
                new CreateNodeCommand.TreeNode(
                        "test_child",
                        "directory",
                        false,
                        rootNodeId
                )
        );

        String childNodeId = treeNodeAppService.create(childNodeCreateCommand);
        rootNode = treeNodeRepository.treeNodeOf(new NodeID(rootNodeId));
        assertNotNull(rootNode);
        assertEquals(new NodeID(rootNodeId), rootNode.getId());
        assertEquals(rootNodeCreateCommand.treeNode().name(), rootNode.getName());
        assertEquals(rootNodeCreateCommand.treeNode().type(), rootNode.getType());
        assertEquals(rootNodeCreateCommand.treeNode().expandable(), rootNode.getExpandable());
        assertFalse(rootNode.getChildNodeList().isEmpty());
        assertFalse(rootNode.getLocked());
        assertNull(rootNode.getParentId());

        TreeNode childNode = rootNode.getChildNodeList().get(0);
        assertNotNull(childNode);
        assertEquals(new NodeID(childNodeId), childNode.getId());
        assertEquals(childNodeCreateCommand.treeNode().name(), childNode.getName());
        assertEquals(childNodeCreateCommand.treeNode().type(), childNode.getType());
        assertEquals(childNodeCreateCommand.treeNode().expandable(), childNode.getExpandable());
        assertEquals(new NodeID(rootNodeId), childNode.getParentId());
        assertTrue(childNode.getChildNodeList().isEmpty());
        assertFalse(childNode.getLocked());

    }


    @Test
    void should_rename_node_normally() {
        assertThrows(ApplicationValidationException.class, () -> new RenameNodeCommand(
                "11",
                new RenameNodeCommand.TreeNode(
                        "1",
                        "  "//newName should not be blank,
                )
        ));

        assertThrows(ApplicationValidationException.class, () -> treeNodeAppService.rename(
                new RenameNodeCommand(
                        "111",
                        new RenameNodeCommand.TreeNode(
                                "222",
                                "new name"
                        )
                )
        ));


        String rootNodeId = treeNodeAppService.create(
                new CreateNodeCommand(
                        null,
                        new CreateNodeCommand.TreeNode(
                                "IamRoot",
                                "directory",
                                true,
                                null
                        )
                )
        );

        assertThrows(ApplicationDomainException.class, () -> treeNodeAppService.rename(
                new RenameNodeCommand(
                        rootNodeId,
                        new RenameNodeCommand.TreeNode(
                                rootNodeId,
                                "RenameRootNode"
                        )
                )
        ));

        final String childNodeName = "IamChild";
        String childNodeId = treeNodeAppService.create(
                new CreateNodeCommand(
                        rootNodeId,
                        new CreateNodeCommand.TreeNode(
                                childNodeName,
                                "directory",
                                true,
                                rootNodeId
                        )
                )
        );

        final String childNodeNewName = "IamChildNew";
        treeNodeAppService.rename(
                new RenameNodeCommand(
                        rootNodeId,
                        new RenameNodeCommand.TreeNode(
                                childNodeId,
                                childNodeNewName
                        )
                )
        );

        TreeNode rootNode = treeNodeRepository.treeNodeOf(new NodeID(rootNodeId));
        assertEquals(childNodeNewName, rootNode.getChildNodeList().get(0).getName());
    }

    @Test
    public void should_drop_node_normally() {
        String rootNodeId = treeNodeAppService.create(
                new CreateNodeCommand(
                        null,
                        new CreateNodeCommand.TreeNode(
                                "IamRootNode",
                                "directory",
                                true,
                                null
                        )
                )
        );


        assertThrows(ApplicationDomainException.class, () -> treeNodeAppService.drop(
                new DropNodeCommand(
                        rootNodeId,
                        new DropNodeCommand.TreeNode(
                                rootNodeId
                        )
                )
        ));

        String childNodeId = treeNodeAppService.create(
                new CreateNodeCommand(
                        rootNodeId,
                        new CreateNodeCommand.TreeNode(
                                "IamChildNode",
                                "directory",
                                true,
                                rootNodeId
                        )
                )
        );

        TreeNode rootNode = treeNodeRepository.treeNodeOf(new NodeID(rootNodeId));
        assertFalse(rootNode.getChildNodeList().isEmpty());

        treeNodeAppService.drop(
                new DropNodeCommand(
                        rootNodeId,
                        new DropNodeCommand.TreeNode(
                                childNodeId
                        )
                )
        );

        rootNode = treeNodeRepository.treeNodeOf(new NodeID(rootNodeId));
        assertTrue(rootNode.getChildNodeList().isEmpty());
    }
}