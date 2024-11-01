package indi.melon.ssc.draft.south.factory;

import indi.melon.ssc.draft.domain.draft.Draft;
import indi.melon.ssc.draft.domain.draft.DraftID;
import indi.melon.ssc.draft.domain.south.factory.DraftFactory;
import indi.melon.ssc.draft.domain.template.Template;
import indi.melon.ssc.ticket.north.local.appservice.TicketAppService;
import indi.melon.ssc.ticket.north.local.message.TicketCreateCommand;
import org.springframework.stereotype.Component;

import static indi.melon.ssc.directory.common.BizTagProperties.DRAFT_BIZ_TAG;

/**
 * @author vvnn1
 * @since 2024/10/28 0:48
 */
@Component
public class DraftFactoryImpl implements DraftFactory {
    private final TicketAppService ticketAppService;

    public DraftFactoryImpl(TicketAppService ticketAppService) {
        this.ticketAppService = ticketAppService;
    }

    @Override
    public Draft create(String name, Template template, String creator) {
        String id = ticketAppService.require(
                new TicketCreateCommand<>(
                        DRAFT_BIZ_TAG,
                        String.class
                )
        );
        return new Draft(
                new DraftID(id),
                name,
                template,
                creator
        );
    }

    @Override
    public Draft create(String name, Draft draft, String creator) {
        String id = ticketAppService.require(
                new TicketCreateCommand<>(
                        DRAFT_BIZ_TAG,
                        String.class
                )
        );

        return new Draft(
                new DraftID(id),
                name,
                draft,
                creator
        );
    }
}
