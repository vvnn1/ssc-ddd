package indi.melon.ssc.domain.draft.configuration;

import indi.melon.ssc.domain.common.cqrs.DomainEvent;
import indi.melon.ssc.domain.draft.configuration.event.AttachmentAllocated;
import indi.melon.ssc.domain.draft.configuration.event.AttachmentDeallocated;
import indi.melon.ssc.domain.draft.configuration.event.EngineAllocated;
import indi.melon.ssc.domain.draft.configuration.event.EngineDeallocated;
import indi.melon.ssc.domain.draft.draft.DraftID;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vvnn1
 * @since 2024/10/3 20:15
 */
public class ConfigurationTest {
    @Test
    public void should_assign_attachments_and_publish_events() {
        Configuration configuration = createConfiguration();
        assertArrayEquals(new AttachmentID[]{new AttachmentID("1"), new AttachmentID("2")}, configuration.getAttachmentIDList().toArray());

        configuration.assignAttachments(Arrays.asList(new AttachmentID("2"), new AttachmentID("3")));
        assertArrayEquals(new AttachmentID[]{new AttachmentID("2"), new AttachmentID("3")}, configuration.getAttachmentIDList().toArray());
        for (DomainEvent domainEvent : configuration.domainEvents()) {
            if(domainEvent instanceof AttachmentAllocated allocated) {
                assertEquals(new ConfigurationID("1"), allocated.getConfigurationId());
                assertEquals(new AttachmentID("3"), allocated.getAttachmentId());
            }

            if (domainEvent instanceof AttachmentDeallocated deallocated){
                assertEquals(new ConfigurationID("1"), deallocated.getConfigurationId());
                assertEquals(new AttachmentID("1"), deallocated.getAttachmentId());
            }
        }

    }

    @Test
    public void should_assign_engin_and_publish_events() {
        Configuration configuration = createConfiguration();
        assertArrayEquals(new AttachmentID[]{new AttachmentID("1"), new AttachmentID("2")}, configuration.getAttachmentIDList().toArray());

        configuration.assignEngine(new EngineID("1"));
        assertTrue(configuration.domainEvents().isEmpty());

        configuration.assignEngine(new EngineID("2"));
        assertEquals(new EngineID("2"), configuration.getEngineID());
        for (DomainEvent domainEvent : configuration.domainEvents()) {
            if (domainEvent instanceof EngineAllocated allocated){
                assertEquals(new ConfigurationID("1"), allocated.getConfigurationID());
                assertEquals(new EngineID("2"), configuration.getEngineID());
            }

            if (domainEvent instanceof EngineDeallocated deallocated) {
                assertEquals(new ConfigurationID("1"), deallocated.getConfigurationID());
                assertEquals(new EngineID("1"), deallocated.getEngineID());
            }
        }
    }

    private Configuration createConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setId(new ConfigurationID("1"));
        configuration.setEngineID(new EngineID("1"));
        configuration.setAttachmentIDList(
                Arrays.asList(
                        new AttachmentID("1"),
                        new AttachmentID("2")
                )
        );
        configuration.setDraftID(new DraftID("1"));
        return configuration;
    }
}
