package indi.melon.ssc.directory.south.factory;

import indi.melon.ssc.directory.common.BizTagProperties;
import indi.melon.ssc.directory.domain.south.factory.TreeNodeFactory;
import indi.melon.ssc.directory.domain.tree.NodeID;
import indi.melon.ssc.directory.domain.tree.TreeNode;
import indi.melon.ssc.ticket.north.local.appservice.TicketAppService;
import indi.melon.ssc.ticket.north.local.message.TicketCreateCommand;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static indi.melon.ssc.directory.common.BizTagProperties.TREE_NODE_BIZ_TAG;

/**
 * @author vvnn1
 * @since 2024/10/5 22:25
 */
@Component
public class TreeNodeFactoryImpl implements TreeNodeFactory {
    private final TicketAppService ticketAppService;

    public TreeNodeFactoryImpl(TicketAppService ticketAppService) {
        this.ticketAppService = ticketAppService;
    }

    @Override
    public TreeNode create(String name, String type, Boolean expandable, String parentNodeId) {
        String id = ticketAppService.require(
                new TicketCreateCommand<>(TREE_NODE_BIZ_TAG, String.class)
        );

        return new TreeNode(
                new NodeID(id),
                name,
                type,
                new ArrayList<>(),
                new NodeID(parentNodeId),
                expandable
        );
    }
}
