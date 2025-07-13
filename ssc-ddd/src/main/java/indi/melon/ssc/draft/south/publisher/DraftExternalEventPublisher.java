package indi.melon.ssc.draft.south.publisher;

import indi.melon.ssc.common.event.IntegrationEvent;
import indi.melon.ssc.common.event.IntegrationEventPublisher;
import indi.melon.ssc.domain.common.cqrs.DomainEventListener;
import indi.melon.ssc.draft.domain.draft.event.AttachmentAllocated;
import indi.melon.ssc.draft.domain.draft.event.AttachmentDeallocated;
import indi.melon.ssc.draft.domain.draft.event.EngineAllocated;
import indi.melon.ssc.draft.domain.draft.event.EngineDeallocated;
import indi.melon.ssc.draft.south.publisher.event.XAttachmentAllocatedConverter;
import indi.melon.ssc.draft.south.publisher.event.XAttachmentDeallocatedConverter;
import indi.melon.ssc.draft.south.publisher.event.XEngineAllocatedConverter;
import indi.melon.ssc.draft.south.publisher.event.XEngineDeallocatedConverter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author wangmenglong
 * @since 2025/5/26 14:30
 */
@Component
public class DraftExternalEventPublisher {
    private final IntegrationEventPublisher integrationEventPublisher;

    public DraftExternalEventPublisher(IntegrationEventPublisher integrationEventPublisher) {
        this.integrationEventPublisher = integrationEventPublisher;
    }

    void publisher(IntegrationEvent event) {
        this.integrationEventPublisher.publisher(event);
    }
    @Component
    public class EngineAllocatedListener implements DomainEventListener<EngineAllocated> {
        @Override
        @TransactionalEventListener
        public void onEvent(EngineAllocated event) {
            publisher(XEngineAllocatedConverter.MAPPER.from(event, "TEST_TOPIC_X_ENGINE_ALLOCATED", null, null));
        }
    }

    @Component
    public class EngineDeallocatedListener implements DomainEventListener<EngineDeallocated> {
        @Override
        @TransactionalEventListener
        public void onEvent(EngineDeallocated event) {
            publisher(XEngineDeallocatedConverter.MAPPER.from(event, "TEST_TOPIC_X_ENGINE_DEALLOCATED", null, null));
        }
    }

    @Component
    public class AttachmentAllocatedListener implements DomainEventListener<AttachmentAllocated> {
        @Override
        @TransactionalEventListener
        public void onEvent(AttachmentAllocated event) {
            publisher(XAttachmentAllocatedConverter.MAPPER.from(event, "TEST_TOPIC_X_ATTACHMENT_ALLOCATED", null, null));
        }
    }

    @Component
    public class AttachmentDeallocatedListener implements DomainEventListener<AttachmentDeallocated> {
        @Override
        @TransactionalEventListener
        public void onEvent(AttachmentDeallocated event) {
            publisher(XAttachmentDeallocatedConverter.MAPPER.from(event, "TEST_TOPIC_X_ATTACHMENT_DEALLOCATED", null, null));
        }
    }

}
