package indi.melon.ssc.draft.domain.configuration;

import indi.melon.ssc.domain.common.cqrs.DomainEvent;
import indi.melon.ssc.draft.domain.configuration.event.AttachmentAllocated;
import indi.melon.ssc.draft.domain.configuration.event.AttachmentDeallocated;
import indi.melon.ssc.draft.domain.configuration.event.EngineAllocated;
import indi.melon.ssc.draft.domain.configuration.event.EngineDeallocated;
import indi.melon.ssc.draft.domain.draft.DraftID;
import org.junit.jupiter.api.Assertions;
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
                Assertions.assertEquals(new ConfigurationID("1"), allocated.getConfigurationId());
                Assertions.assertEquals(new AttachmentID("3"), allocated.getAttachmentId());
            }

            if (domainEvent instanceof AttachmentDeallocated deallocated){
                Assertions.assertEquals(new ConfigurationID("1"), deallocated.getConfigurationId());
                Assertions.assertEquals(new AttachmentID("1"), deallocated.getAttachmentId());
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
                Assertions.assertEquals(new ConfigurationID("1"), allocated.getConfigurationID());
                assertEquals(new EngineID("2"), configuration.getEngineID());
            }

            if (domainEvent instanceof EngineDeallocated deallocated) {
                Assertions.assertEquals(new ConfigurationID("1"), deallocated.getConfigurationID());
                Assertions.assertEquals(new EngineID("1"), deallocated.getEngineID());
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
