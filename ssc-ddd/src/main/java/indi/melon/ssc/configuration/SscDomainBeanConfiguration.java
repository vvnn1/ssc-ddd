package indi.melon.ssc.configuration;

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
}
