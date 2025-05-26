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
    public void should_equals_and_hashcode() {
        Configuration configuration1 = new Configuration(
                new EngineID("1"),
                Arrays.asList(
                        new AttachmentID("1"),
                        new AttachmentID("2")
                )
        );

        Configuration configuration2 = new Configuration(
                new EngineID("1"),
                Arrays.asList(
                        new AttachmentID("1"),
                        new AttachmentID("2")
                )
        );

        Configuration configuration3 = new Configuration(
                new EngineID("1"),
                List.of(
                        new AttachmentID("1")
                )
        );

        Configuration configuration4 = new Configuration(
                new EngineID("2"),
                List.of(
                        new AttachmentID("1"),
                        new AttachmentID("2")
                )
        );

        assertEquals(configuration1.hashCode(), configuration2.hashCode());
        assertEquals(configuration1, configuration2);

        assertNotEquals(configuration1.hashCode(), configuration3.hashCode());
        assertNotEquals(configuration1, configuration3);

        assertNotEquals(configuration1.hashCode(), configuration4.hashCode());
        assertNotEquals(configuration1, configuration4);
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

        assertArrayEquals(new AttachmentID[]{new AttachmentID("1"), new AttachmentID("2"), new AttachmentID("3")}, config1.currentAttachmentIds().toArray());

        Configuration config2 = config1.assignAttachments(
                Arrays.asList(
                        new AttachmentID("2"),
                        new AttachmentID("4")
                )
        );
        assertArrayEquals(new AttachmentID[]{new AttachmentID("2"), new AttachmentID("4")}, config2.currentAttachmentIds().toArray());

        Configuration config3 = config2.assignAttachments(Collections.emptyList());
        assertArrayEquals(new AttachmentID[0], config3.currentAttachmentIds().toArray());
    }

    @Test
    public void should_assign_engine_normally() {
        Configuration configuration = new Configuration();

        Configuration config1 = configuration.assignEngine(new EngineID("1"));
        assertEquals(new EngineID("1"), config1.currentEngineId());

        Configuration config2 = config1.assignEngine(new EngineID("2"));
        assertEquals(new EngineID("2"), config2.currentEngineId());

        Configuration config3 = config2.assignEngine(null);
        assertNull(config3.currentEngineId());
    }
}
