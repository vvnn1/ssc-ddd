package indi.melon.ssc.draft.domain.draft;

import indi.melon.ssc.draft.domain.exception.NotMatchException;
import indi.melon.ssc.draft.domain.template.Template;
import indi.melon.ssc.draft.domain.version.Version;
import indi.melon.ssc.draft.domain.version.VersionID;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static indi.melon.ssc.draft.domain.template.TemplateUtil.buildTemplate;
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

        assertThrows(NotMatchException.class, () -> draft.rollback(badVersion, "vvnn1"));

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
        LocalDateTime beforeCreate = LocalDateTime.now();
        Draft copyDraft = draft.copy(new DraftID(newID), newName, modifier);
        LocalDateTime afterCreate = LocalDateTime.now();
        assertEquals(new DraftID(newID), copyDraft.getId());
        assertEquals(newName, copyDraft.getName());
        assertEquals(modifier, copyDraft.getCreator());
        assertEquals(modifier, copyDraft.getModifier());
        assertEquals(draft.getContent(), copyDraft.getContent());
        assertTrue(beforeCreate.isBefore(copyDraft.getCreateTime()));
        assertTrue(afterCreate.isAfter(copyDraft.getCreateTime()));
        assertTrue(beforeCreate.isBefore(copyDraft.getUpdateTime()));
        assertTrue(afterCreate.isAfter(copyDraft.getUpdateTime()));
    }

    @Test
    public void should_build_draft_from_template(){
        Template template = buildTemplate("templateId1", "testContent");
        Draft draft = new Draft(
                new DraftID("draftId"),
                "draftName",
                template,
                "creator11"
        );
        assertEquals(new DraftID("draftId"), draft.getId());
        assertEquals("draftName", draft.getName());
        assertEquals("testContent", draft.getContent());
        assertEquals(DraftCatalog.STREAM, draft.getCatalog());
        assertEquals(DraftType.SQL, draft.getType());
        assertEquals("creator11", draft.getCreator());
        assertEquals("creator11", draft.getModifier());
    }

    private Draft buildDraft(){
        Draft draft = new Draft(
                new DraftID("DraftID1"),
                "testName",
                "TestContent",
                DraftCatalog.STREAM,
                DraftType.SQL,
                "Melon"
        );
        draft.setUpdateTime(LocalDateTime.of(2023, 10, 25, 1, 2));
        return draft;
    }
}
