package indi.melon.ssc.configuration;

import indi.melon.ssc.draft.domain.draft.DraftManager;
import indi.melon.ssc.draft.domain.south.factory.DraftFactory;
import indi.melon.ssc.ticket.domain.south.repository.TicketSegmentRepository;
import indi.melon.ssc.resource.domain.resource.ResourceManager;
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
    public TicketManager ticketManager(TicketSegmentRepository ticketSegmentRepository) {
        return new TicketManager(ticketSegmentRepository, 2);
    }

    @Bean
    public DraftManager draftManager(DraftFactory draftFactory) {
        return new DraftManager(draftFactory);
    }

    @Bean
    public ResourceManager resourceManager() {
        return new ResourceManager();
    }
}
