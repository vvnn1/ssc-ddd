package indi.melon.ssc.configuration;

import indi.melon.ssc.directory.domain.south.factory.TreeNodeFactory;
import indi.melon.ssc.directory.domain.south.repository.TreeNodeRepository;
import indi.melon.ssc.directory.domain.tree.TreeNodeManager;
import indi.melon.ssc.draft.domain.draft.DraftManager;
import indi.melon.ssc.draft.domain.south.factory.DraftFactory;
import indi.melon.ssc.draft.domain.south.repository.DraftRepository;
import indi.melon.ssc.draft.domain.south.repository.TemplateRepository;
import indi.melon.ssc.draft.domain.south.repository.VersionRepository;
import indi.melon.ssc.ticket.domain.south.repository.TicketBoxRepository;
import indi.melon.ssc.ticket.domain.ticket.BoxManager;
import indi.melon.ssc.ticket.domain.ticket.TicketManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangmenglong
 * @since 2024/10/17 20:17
 */
@Configuration
public class SscDomainBeanConfiguration {
    @Bean
    public BoxManager boxManager(TicketBoxRepository ticketBoxRepository) {
        return new BoxManager(ticketBoxRepository);
    }

    @Bean
    public TicketManager ticketManager(BoxManager boxManager) {
        return new TicketManager(boxManager);
    }

    @Bean
    public DraftManager draftManager(DraftFactory draftFactory) {
        return new DraftManager(draftFactory);
    }
}
