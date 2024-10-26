package indi.melon.ssc.draft.domain.version;

import indi.melon.ssc.draft.domain.draft.DraftID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vvnn1
 * @since 2024/10/26 14:12
 */
public class VersionTest {
    @Test
    public void should_lock_and_unlock_normally() {
        Version version = buildVersion();
        assertFalse(version.isLocked());

        version.lock();
        assertTrue(version.isLocked());

        version.unlock();
        assertFalse(version.isLocked());
    }

    private Version buildVersion(){
        return new Version(
                new VersionID("VersionID"),
                new DraftID("DraftID"),
                "testContent",
                "remark",
                "Melon"
        );
    }
}
