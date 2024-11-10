package indi.melon.ssc.draft.domain.configuration;

import indi.melon.ssc.draft.domain.configuration.event.AttachmentAllocated;
import indi.melon.ssc.draft.domain.configuration.event.AttachmentDeallocated;
import indi.melon.ssc.draft.domain.configuration.event.EngineAllocated;
import indi.melon.ssc.draft.domain.configuration.event.EngineDeallocated;
import indi.melon.ssc.draft.domain.draft.DraftID;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vvnn1
 * @since 2024/10/3 20:15
 */
public class ConfigurationTest {
    @Test
    public void should_assign_attachments_and_publish_events() {
        Configuration configuration = new Configuration(
                new ConfigurationID("ConfigurationID1")
        );


        configuration.clearDomainEvents();
        configuration.setAttachmentIDCollection(null);

        configuration.assignAttachments(
                Arrays.asList(
                        new AttachmentID("AttachmentID1"),
                        new AttachmentID("AttachmentID2")
                )
        );
        assertTrue(configuration.domainEvents().contains(new AttachmentAllocated(new ConfigurationID("ConfigurationID1"), new AttachmentID("AttachmentID1"))));
        assertTrue(configuration.domainEvents().contains(new AttachmentAllocated(new ConfigurationID("ConfigurationID1"), new AttachmentID("AttachmentID2"))));

        configuration.clearDomainEvents();
        configuration.setAttachmentIDCollection(
                new HashSet<>(
                        Arrays.asList(
                                new AttachmentID("AttachmentID1"),
                                new AttachmentID("AttachmentID2")
                        )
                )
        );

        configuration.assignAttachments(
                Arrays.asList(
                        new AttachmentID("AttachmentID2"),
                        new AttachmentID("AttachmentID3")
                )
        );
        assertTrue(configuration.domainEvents().contains(new AttachmentDeallocated(new ConfigurationID("ConfigurationID1"), new AttachmentID("AttachmentID1"))));
        assertTrue(configuration.domainEvents().contains(new AttachmentAllocated(new ConfigurationID("ConfigurationID1"), new AttachmentID("AttachmentID3"))));


        configuration.clearDomainEvents();
        configuration.setAttachmentIDCollection(
                new HashSet<>(
                        Arrays.asList(
                                new AttachmentID("AttachmentID1"),
                                new AttachmentID("AttachmentID2")
                        )
                )
        );

        configuration.assignAttachments(
                null
        );
        assertTrue(configuration.domainEvents().contains(new AttachmentDeallocated(new ConfigurationID("ConfigurationID1"), new AttachmentID("AttachmentID1"))));
        assertTrue(configuration.domainEvents().contains(new AttachmentDeallocated(new ConfigurationID("ConfigurationID1"), new AttachmentID("AttachmentID2"))));
    }

    @Test
    public void should_assign_engin_and_publish_events() {
        Configuration configuration = new Configuration(
                new ConfigurationID("ConfigurationID1")
        );

        configuration.assignEngine(new EngineID("EngineID1"));
        assertTrue(configuration.domainEvents().contains(new EngineAllocated(new ConfigurationID("ConfigurationID1"), new EngineID("EngineID1"))));

        configuration.clearDomainEvents();
        configuration.setEngineID(new EngineID("EngineID1"));

        configuration.assignEngine(null);
        assertTrue(configuration.domainEvents().contains(new EngineDeallocated(new ConfigurationID("ConfigurationID1"), new EngineID("EngineID1"))));


        configuration.clearDomainEvents();
        configuration.setEngineID(new EngineID("EngineID1"));

        configuration.assignEngine(new EngineID("EngineID2"));
        assertTrue(configuration.domainEvents().contains(new EngineDeallocated(new ConfigurationID("ConfigurationID1"), new EngineID("EngineID1"))));
        assertTrue(configuration.domainEvents().contains(new EngineAllocated(new ConfigurationID("ConfigurationID1"), new EngineID("EngineID2"))));
    }

    @Test
    public void should_build_from_draft_id() {
        Configuration configuration = new Configuration(new DraftID("testDraft"));
        assertEquals("testDraft", configuration.getId().value);
    }
}
