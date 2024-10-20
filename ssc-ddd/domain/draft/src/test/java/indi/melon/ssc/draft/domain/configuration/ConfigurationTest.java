package indi.melon.ssc.draft.domain.configuration;

import indi.melon.ssc.draft.domain.configuration.event.AttachmentAllocated;
import indi.melon.ssc.draft.domain.configuration.event.AttachmentDeallocated;
import indi.melon.ssc.draft.domain.configuration.event.EngineAllocated;
import indi.melon.ssc.draft.domain.configuration.event.EngineDeallocated;
import indi.melon.ssc.draft.domain.draft.DraftID;
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
        Configuration configuration = new Configuration(
                new ConfigurationID("ConfigurationID1"),
                new DraftID("DraftID1")
        );


        configuration.clearDomainEvents();
        configuration.setAttachmentIDCollection(null);

        configuration.assignAttachments(
                Arrays.asList(
                        new AttachmentID("AttachmentID1"),
                        new AttachmentID("AttachmentID2")
                )
        );
        assertTrue(configuration.domainEvents().contains(new AttachmentAllocated("ConfigurationID1", "AttachmentID1")));
        assertTrue(configuration.domainEvents().contains(new AttachmentAllocated("ConfigurationID1", "AttachmentID2")));

        configuration.clearDomainEvents();
        configuration.setAttachmentIDCollection(
                Arrays.asList(
                        new AttachmentID("AttachmentID1"),
                        new AttachmentID("AttachmentID2")
                )
        );

        configuration.assignAttachments(
                Arrays.asList(
                        new AttachmentID("AttachmentID2"),
                        new AttachmentID("AttachmentID3")
                )
        );
        assertTrue(configuration.domainEvents().contains(new AttachmentDeallocated("ConfigurationID1", "AttachmentID1")));
        assertTrue(configuration.domainEvents().contains(new AttachmentAllocated("ConfigurationID1", "AttachmentID3")));


        configuration.clearDomainEvents();
        configuration.setAttachmentIDCollection(
                Arrays.asList(
                        new AttachmentID("AttachmentID1"),
                        new AttachmentID("AttachmentID2")
                )
        );

        configuration.assignAttachments(
                null
        );
        assertTrue(configuration.domainEvents().contains(new AttachmentDeallocated("ConfigurationID1", "AttachmentID1")));
        assertTrue(configuration.domainEvents().contains(new AttachmentDeallocated("ConfigurationID1", "AttachmentID2")));
    }

    @Test
    public void should_assign_engin_and_publish_events() {
        Configuration configuration = new Configuration(
                new ConfigurationID("ConfigurationID1"),
                new DraftID("DraftID1")
        );

        configuration.assignEngine(new EngineID("EngineID1"));
        assertTrue(configuration.domainEvents().contains(new EngineAllocated("ConfigurationID1", "EngineID1")));

        configuration.clearDomainEvents();
        configuration.setEngineID(new EngineID("EngineID1"));

        configuration.assignEngine(null);
        assertTrue(configuration.domainEvents().contains(new EngineDeallocated("ConfigurationID1", "EngineID1")));


        configuration.clearDomainEvents();
        configuration.setEngineID(new EngineID("EngineID1"));

        configuration.assignEngine(new EngineID("EngineID2"));
        assertTrue(configuration.domainEvents().contains(new EngineDeallocated("ConfigurationID1", "EngineID1")));
        assertTrue(configuration.domainEvents().contains(new EngineAllocated("ConfigurationID1", "EngineID2")));
    }
}
