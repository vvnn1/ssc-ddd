package indi.melon.ssc.directory.north.local.appservice;

import indi.melon.ssc.SscBaseTest;
import indi.melon.ssc.common.exception.ApplicationDomainException;
import indi.melon.ssc.common.exception.ApplicationValidationException;
import indi.melon.ssc.directory.common.BizTagProperties;
import indi.melon.ssc.directory.domain.south.repository.TreeNodeRepository;
import indi.melon.ssc.directory.domain.tree.NodeID;
import indi.melon.ssc.directory.domain.tree.TreeNode;
import indi.melon.ssc.directory.domain.tree.exception.NotFoundException;
import indi.melon.ssc.directory.domain.tree.exception.NotSupportException;
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
                "iamroot", //should not have rootNodeId
                "directory",
                true,
                true,
                "111"
        ));

        assertThrows(ApplicationValidationException.class, () -> new CreateNodeCommand(
                "iamroot",
                "directory",
                true,
                false,
                null //child node should assign rootNodeId
        ));

        assertThrows(NotFoundException.class, () -> treeNodeAppService.create(
                new CreateNodeCommand(
                        "iamroot", // not found rootNode
                        "directory",
                        true,
                        false,
                        "111"
                )
        ));

        CreateNodeCommand rootNodeCreateCommand = new CreateNodeCommand(
                "test_root",
                "directory",
                true,
                true,
                null
        );

        String rootNodeId = treeNodeAppService.create(rootNodeCreateCommand);
        assertNotNull(rootNodeId);
        assertTrue(rootNodeId.startsWith(TREE_NODE_ID_PREFIX));

        TreeNode rootNode = treeNodeRepository.treeNodeOf(
                new NodeID(rootNodeId)
        );
        assertNotNull(rootNode);
        assertEquals(new NodeID(rootNodeId), rootNode.getId());
        assertEquals(rootNodeCreateCommand.name(), rootNode.getName());
        assertEquals(rootNodeCreateCommand.type(), rootNode.getType());
        assertEquals(rootNodeCreateCommand.expandable(), rootNode.getExpandable());
        assertTrue(rootNode.getChildNodeList() == null || rootNode.getChildNodeList().isEmpty());
        assertNull(rootNode.getParentNode());

        CreateNodeCommand childNodeCreateCommand = new CreateNodeCommand(
                "test_child",
                "directory",
                false,
                false,
                rootNodeId
        );

        String childNodeId = treeNodeAppService.create(childNodeCreateCommand);
        assertTrue(childNodeId.startsWith(TREE_NODE_ID_PREFIX));

        rootNode = treeNodeRepository.treeNodeOf(new NodeID(rootNodeId));
        assertNotNull(rootNode);
        assertEquals(new NodeID(rootNodeId), rootNode.getId());
        assertEquals(rootNodeCreateCommand.name(), rootNode.getName());
        assertEquals(rootNodeCreateCommand.type(), rootNode.getType());
        assertEquals(rootNodeCreateCommand.expandable(), rootNode.getExpandable());
        assertFalse(rootNode.getChildNodeList().isEmpty());
        assertFalse(rootNode.getLocked());
        assertNull(rootNode.getParentNode());

        TreeNode childNode = rootNode.getChildNodeList().get(0);
        assertNotNull(childNode);
        assertEquals(new NodeID(childNodeId), childNode.getId());
        assertEquals(childNodeCreateCommand.name(), childNode.getName());
        assertEquals(childNodeCreateCommand.type(), childNode.getType());
        assertEquals(childNodeCreateCommand.expandable(), childNode.getExpandable());
        assertEquals(rootNode, childNode.getParentNode());
        assertTrue(childNode.getChildNodeList() == null || childNode.getChildNodeList().isEmpty());
        assertFalse(childNode.getLocked());

    }


    @Test
    void should_rename_node_normally() {
        assertThrows(ApplicationValidationException.class, () -> new RenameNodeCommand(
                "1",
                "  "//newName should not be blank,
        ));

        assertThrows(ApplicationDomainException.class, () -> treeNodeAppService.rename(
                new RenameNodeCommand(
                        "222",
                        "new name"
                )
        ));


        String rootNodeId = treeNodeAppService.create(
                new CreateNodeCommand(
                        "IamRoot",
                        "directory",
                        true,
                        true,
                        null
                )
        );

        assertThrows(ApplicationDomainException.class, () -> treeNodeAppService.rename(
                new RenameNodeCommand(
                        rootNodeId,
                        "RenameRootNode"
                )
        ));

        final String childNodeName = "IamChild";
        String childNodeId = treeNodeAppService.create(
                new CreateNodeCommand(
                        childNodeName,
                        "directory",
                        true,
                        false,
                        rootNodeId
                )
        );

        final String childNodeNewName = "IamChildNew";
        treeNodeAppService.rename(
                new RenameNodeCommand(
                        childNodeId,
                        childNodeNewName
                )
        );

        TreeNode rootNode = treeNodeRepository.treeNodeOf(new NodeID(rootNodeId));
        assertEquals(childNodeNewName, rootNode.getChildNodeList().get(0).getName());
    }

    @Test
    public void should_drop_node_normally() {
        String rootNodeId = treeNodeAppService.create(
                new CreateNodeCommand(
                        "IamRootNode",
                        "directory",
                        true,
                        true,
                        null
                )
        );


        assertThrows(ApplicationDomainException.class, () -> treeNodeAppService.drop(
                new DropNodeCommand(
                        rootNodeId
                )
        ));

        String childNodeId = treeNodeAppService.create(
                new CreateNodeCommand(
                        "IamChildNode",
                        "directory",
                        true,
                        false,
                        rootNodeId
                )
        );

        String childNodeId2 = treeNodeAppService.create(
                new CreateNodeCommand(
                        "IamChildNode2",
                        "file",
                        false,
                        false,
                        childNodeId
                )
        );

        TreeNode rootNode = treeNodeRepository.treeNodeOf(new NodeID(rootNodeId));
        assertFalse(rootNode.getChildNodeList().isEmpty());

        treeNodeAppService.drop(
                new DropNodeCommand(
                        childNodeId
                )
        );

        rootNode = treeNodeRepository.treeNodeOf(rootNode.getId());
        assertTrue(rootNode.getChildNodeList().isEmpty());

        assertNull(treeNodeRepository.treeNodeOf(new NodeID(childNodeId)));
        assertNull(treeNodeRepository.treeNodeOf(new NodeID(childNodeId2)));
    }

    @Test
    public void should_move_node_normally() {
        String rootNodeId = treeNodeAppService.create(new CreateNodeCommand(
                "IamRootNode",
                "directory",
                true,
                true,
                null
        ));


        String childNodeId1 = treeNodeAppService.create(new CreateNodeCommand(
                "IamChildNode1",
                "directory",
                true,
                false,
                rootNodeId
        ));

        String childNodeId2 = treeNodeAppService.create(new CreateNodeCommand(
                "IamChildNode2",
                "directory",
                true,
                false,
                rootNodeId
        ));

        treeNodeAppService.move(new MoveNodeCommand(
                childNodeId2,
                childNodeId1
        ));


        TreeNode rootNode = treeNodeRepository.treeNodeOf(new NodeID(rootNodeId));
        assertEquals(1, rootNode.getChildNodeList().size());
        TreeNode childNode1 = rootNode.getChildNodeList().get(0);
        assertEquals("IamChildNode1", childNode1.getName());

        assertThrows(ApplicationDomainException.class, () -> {
            treeNodeAppService.move(new MoveNodeCommand(
                    "notExistId",
                    childNodeId2
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

