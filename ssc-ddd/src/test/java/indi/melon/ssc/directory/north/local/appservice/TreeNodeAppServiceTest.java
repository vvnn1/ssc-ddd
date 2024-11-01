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
import indi.melon.ssc.directory.north.local.message.MoveNodeCommand;
import indi.melon.ssc.directory.north.local.message.RenameNodeCommand;
import indi.melon.ssc.directory.south.repository.MockTreeNodeRepository;
import indi.melon.ssc.ticket.north.local.appservice.TicketAppService;
import indi.melon.ssc.ticket.north.local.message.TicketCreateCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author wangmenglong
 * @since 2024/10/17 19:26
 */
@Import(TreeNodeAppServiceTest.MockConfiguration.class)
class TreeNodeAppServiceTest extends SscBaseTest {

    @Autowired
    private TreeNodeAppService treeNodeAppService;
    @Autowired
    private TreeNodeRepository treeNodeRepository;

    @MockBean
    private TicketAppService ticketAppService;

    private static final String TREE_NODE_ID_PREFIX = "MockTreeNodeID-";
    @BeforeEach
    void initMock() {
        Mockito.clearInvocations(ticketAppService);
        AtomicInteger integer = new AtomicInteger(0);
        Mockito.when(ticketAppService.require(new TicketCreateCommand<>(
                BizTagProperties.TREE_NODE_BIZ_TAG,
                String.class
        ))).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return TREE_NODE_ID_PREFIX+integer.incrementAndGet();
            }
        });
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
        assertTrue(rootNodeId.startsWith(TREE_NODE_ID_PREFIX));

        TreeNode rootNode = treeNodeRepository.treeNodeOf(
                new NodeID(rootNodeId)
        );
        assertNotNull(rootNode);
        assertEquals(new NodeID(rootNodeId), rootNode.getId());
        assertEquals(rootNodeCreateCommand.treeNode().name(), rootNode.getName());
        assertEquals(rootNodeCreateCommand.treeNode().type(), rootNode.getType());
        assertEquals(rootNodeCreateCommand.treeNode().expandable(), rootNode.getExpandable());
        assertTrue(rootNode.getChildNodeList() == null || rootNode.getChildNodeList().isEmpty());
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
        assertTrue(childNodeId.startsWith(TREE_NODE_ID_PREFIX));

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
        assertTrue(childNode.getChildNodeList() == null || childNode.getChildNodeList().isEmpty());
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

        String childNodeId2 = treeNodeAppService.create(
                new CreateNodeCommand(
                        rootNodeId,
                        new CreateNodeCommand.TreeNode(
                                "IamChildNode2",
                                "file",
                                false,
                                childNodeId
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

        assertNull(treeNodeRepository.treeNodeOf(new NodeID(childNodeId)));
        assertNull(treeNodeRepository.treeNodeOf(new NodeID(childNodeId2)));
    }

    @Test
    public void should_move_node_normally() {
        String rootNodeId = treeNodeAppService.create(new CreateNodeCommand(
                null,
                new CreateNodeCommand.TreeNode(
                        "IamRootNode",
                        "directory",
                        true,
                        null
                )
        ));


        String childNodeId1 = treeNodeAppService.create(new CreateNodeCommand(
                rootNodeId,
                new CreateNodeCommand.TreeNode(
                        "IamChildNode1",
                        "directory",
                        true,
                        rootNodeId
                )
        ));

        String childNodeId2 = treeNodeAppService.create(new CreateNodeCommand(
                rootNodeId,
                new CreateNodeCommand.TreeNode(
                        "IamChildNode2",
                        "directory",
                        true,
                        rootNodeId
                )
        ));

        treeNodeAppService.move(new MoveNodeCommand(
                rootNodeId,
                new MoveNodeCommand.TreeNode(
                        childNodeId2,
                        childNodeId1
                )
        ));


        TreeNode rootNode = treeNodeRepository.treeNodeOf(new NodeID(rootNodeId));
        assertEquals(1, rootNode.getChildNodeList().size());
        TreeNode childNode1 = rootNode.getChildNodeList().get(0);
        assertEquals("IamChildNode1", childNode1.getName());

        assertThrows(ApplicationDomainException.class, () -> {
            treeNodeAppService.move(new MoveNodeCommand(
                    rootNodeId,
                    new MoveNodeCommand.TreeNode(
                            "notExistId",
                            childNodeId2
                    )
            ));
        });

    }

    @TestConfiguration
    static class MockConfiguration {
        @Bean
        public TreeNodeRepository treeNodeRepository() {
            return new MockTreeNodeRepository();
        }
    }
}

