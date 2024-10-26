package indi.melon.ssc.draft.domain.draft;

import indi.melon.ssc.draft.domain.draft.exception.RollbackVersionNotMatchException;
import indi.melon.ssc.draft.domain.version.Version;
import indi.melon.ssc.draft.domain.version.VersionID;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vvnn1
 * @since 2024/10/26 0:26
 */
public class DraftTest {

    @Test
    public void should_rollback_normally(){
        Draft draft = buildDraft();
        Version badVersion =new Version(
                new VersionID("VersionID1"),
                new DraftID("DraftID2"),
                "VersionContent",
                "IamRemark",
                "Melon"
        );

        assertThrows(RollbackVersionNotMatchException.class, () -> draft.rollback(badVersion, "vvnn1"));

        Version version =new Version(
                new VersionID("VersionID1"),
                new DraftID("DraftID1"),
                "VersionContent",
                "IamRemark",
                "Melon"
        );

        draft.rollback(version, "vvnn1");
        assertEquals("VersionContent", draft.getContent());
        assertEquals("vvnn1", draft.getModifier());
        assertTrue(LocalDateTime.now().withSecond(0).withNano(0).isBefore(draft.getUpdateTime()));
    }

    @Test
    public void should_rename_normally(){
        Draft draft = buildDraft();
        assertEquals("testName", draft.getName());

        draft.rename("newName", "vvnn1");
        assertEquals("newName", draft.getName());
        assertEquals("vvnn1", draft.getModifier());
        assertTrue(LocalDateTime.now().withSecond(0).withNano(0).isBefore(draft.getUpdateTime()));

    }

    @Test
    public void should_edit_normally() {
        Draft draft = buildDraft();
        assertEquals("TestContent", draft.getContent());

        draft.editContent("TestContent222", "vvnn1");
        assertEquals("TestContent222", draft.getContent());
        assertEquals("vvnn1", draft.getModifier());
        assertTrue(LocalDateTime.now().withSecond(0).withNano(0).isBefore(draft.getUpdateTime()));
    }

    @Test
    public void should_copy_normally() {
        Draft draft = buildDraft();
        String newID = "DraftID1";
        String newName = "DraftBakup";
        String modifier = "vvnn1";
        Draft copyDraft = draft.copy(new DraftID(newID), newName, modifier);
        assertEquals(new DraftID(newID), copyDraft.getId());
        assertEquals(newName, copyDraft.getName());
        assertEquals(modifier, copyDraft.getCreator());
        assertEquals(modifier, copyDraft.getModifier());
        assertEquals(draft.getContent(), copyDraft.getContent());
        assertTrue(LocalDateTime.now().withSecond(0).withNano(0).isBefore(copyDraft.getCreateTime()));
        assertTrue(LocalDateTime.now().withSecond(0).withNano(0).isBefore(copyDraft.getUpdateTime()));
    }

    private Draft buildDraft(){
        Draft draft = new Draft(
                new DraftID("DraftID1"),
                "testName",
                DraftType.STREAM,
                "Melon"
        );
        draft.setContent("TestContent");
        draft.setUpdateTime(LocalDateTime.of(2023, 10, 25, 1, 2));
        return draft;
    }
}
