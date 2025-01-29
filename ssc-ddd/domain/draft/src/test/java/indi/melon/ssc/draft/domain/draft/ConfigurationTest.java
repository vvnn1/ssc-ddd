package indi.melon.ssc.draft.domain.draft;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vvnn1
 * @since 2024/10/3 20:15
 */
public class ConfigurationTest {
    @Test
    public void should_compare_configuration_normally() {
        Configuration config1 = new Configuration().assignAttachments(
                Arrays.asList(
                        new AttachmentID("1"),
                        new AttachmentID("2"),
                        new AttachmentID("3"),
                        new AttachmentID("4")
                )
        );

        Configuration config2 = new Configuration().assignAttachments(
                Arrays.asList(
                        new AttachmentID("1"),
                        new AttachmentID("2")
                )
        );

        assertArrayEquals(new AttachmentID[]{new AttachmentID("3"), new AttachmentID("4")}, config1.attachmentsNotIn(config2).toArray());
        assertArrayEquals(new AttachmentID[0], new Configuration().attachmentsNotIn(config1).toArray());
        assertArrayEquals(new AttachmentID[]{new AttachmentID("1"), new AttachmentID("2"), new AttachmentID("3"), new AttachmentID("4")}, config1.attachmentsNotIn(new Configuration()).toArray());
    }

    @Test
    public void should_assign_attachment_normally() {
        Configuration configuration = new Configuration();

        Configuration config1 = configuration.assignAttachments(
                Arrays.asList(
                        new AttachmentID("1"),
                        new AttachmentID("2"),
                        new AttachmentID("3")
                )
        );

        assertArrayEquals(new AttachmentID[]{new AttachmentID("1"), new AttachmentID("2"), new AttachmentID("3")}, config1.getAttachmentIdCollection().toArray());

        Configuration config2 = config1.assignAttachments(
                Arrays.asList(
                        new AttachmentID("2"),
                        new AttachmentID("4")
                )
        );
        assertArrayEquals(new AttachmentID[]{new AttachmentID("2"), new AttachmentID("4")}, config2.getAttachmentIdCollection().toArray());

        Configuration config3 = config2.assignAttachments(Collections.emptyList());
        assertArrayEquals(new AttachmentID[0], config3.getAttachmentIdCollection().toArray());
    }

    @Test
    public void should_assign_engine_normally() {
        Configuration configuration = new Configuration();

        Configuration config1 = configuration.assignEngine(new EngineID("1"));
        assertEquals(new EngineID("1"), config1.getEngineId());

        Configuration config2 = config1.assignEngine(new EngineID("2"));
        assertEquals(new EngineID("2"), config2.getEngineId());

        Configuration config3 = config2.assignEngine(null);
        assertNull(config3.getEngineId());
    }
}
