package indi.melon.ssc.draft.domain.draft;

import indi.melon.ssc.draft.domain.draft.event.AttachmentAllocated;
import indi.melon.ssc.draft.domain.draft.event.AttachmentDeallocated;
import indi.melon.ssc.draft.domain.draft.event.EngineAllocated;
import indi.melon.ssc.draft.domain.draft.event.EngineDeallocated;
import indi.melon.ssc.draft.domain.exception.NotMatchException;
import indi.melon.ssc.draft.domain.template.Template;
import indi.melon.ssc.draft.domain.version.Version;
import indi.melon.ssc.draft.domain.version.VersionID;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static indi.melon.ssc.draft.domain.template.TemplateUtil.buildTemplate;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vvnn1
 * @since 2024/10/26 0:26
 */
public class DraftTest {

    @Test
    public void should_rollback_normally() {
        Draft draft = buildDraft();
        Version badVersion = new Version(
                new VersionID("VersionID1"),
                new DraftID("DraftID2"),
                "VersionContent",
                "IamRemark",
                "Melon"
        );

        assertThrows(NotMatchException.class, () -> draft.rollback(badVersion, "vvnn1"));

        Version version = new Version(
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
    public void should_rename_normally() {
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
    public void should_build_draft_from_template() {
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

    @Test
    public void should_assign_engine_normally() {
        Draft draft = buildDraft();
        DraftID draftId = draft.getId();
        assertNull(draft.getConfiguration().currentEngineId());

        draft.assignEngine(
                new EngineID("1")
        );

        assertTrue(
                draft.domainEvents()
                        .contains(
                                new EngineAllocated(draftId, new EngineID("1"))
                        )
        );
        draft.clearDomainEvents();

        draft.assignEngine(
                new EngineID("1")
        );

        assertTrue(
                draft.domainEvents().isEmpty()
        );

        draft.assignEngine(
                new EngineID("2")
        );

        assertEquals(2, draft.domainEvents().size());
        assertTrue(
                draft.domainEvents()
                        .containsAll(
                                Arrays.asList(
                                        new EngineAllocated(draftId, new EngineID("2")),
                                        new EngineDeallocated(draftId, new EngineID("1"))
                                )
                        )
        );
        draft.clearDomainEvents();

        draft.assignEngine(null);
        assertEquals(1, draft.domainEvents().size());
        assertTrue(
                draft.domainEvents()
                        .contains(
                                new EngineDeallocated(draftId, new EngineID("2"))
                        )
        );
    }

    @Test
    public void should_assign_attachments_normally() {
        Draft draft = buildDraft();
        DraftID draftId = draft.getId();

        assertEquals(0, draft.getConfiguration().currentAttachmentIds().size());
        draft.assignAttachments(
                List.of(
                        new AttachmentID("1")
                )
        );

        assertEquals(1, draft.getConfiguration().currentAttachmentIds().size());
        assertTrue(
                draft.domainEvents()
                        .contains(new AttachmentAllocated(draftId, new AttachmentID("1")))
        );
        draft.clearDomainEvents();

        draft.assignAttachments(
                List.of(
                        new AttachmentID("1"),
                        new AttachmentID("2")
                )
        );
        assertEquals(2, draft.getConfiguration().currentAttachmentIds().size());
        assertTrue(
                draft.domainEvents()
                        .contains(
                                new AttachmentAllocated(draftId, new AttachmentID("2"))
                        )
        );
        draft.clearDomainEvents();

        draft.assignAttachments(
                List.of(
                        new AttachmentID("1")
                )
        );
        assertEquals(1, draft.getConfiguration().currentAttachmentIds().size());
        assertTrue(
                draft.domainEvents()
                        .contains(
                                new AttachmentDeallocated(draftId, new AttachmentID("2"))
                        )
        );
        draft.clearDomainEvents();

        draft.assignAttachments(
                Collections.emptyList()
        );
        assertEquals(0, draft.getConfiguration().currentAttachmentIds().size());
        assertTrue(
                draft.domainEvents()
                        .contains(
                                new AttachmentDeallocated(draftId, new AttachmentID("1"))
                        )
        );
        draft.clearDomainEvents();
    }

    @Test
    public void should_assign_configuration_normally() {
        Draft draft = buildDraft();
        DraftID draftId = draft.getId();

        assertNull(draft.getConfiguration().engineId);
        assertEquals(0, draft.getConfiguration().currentAttachmentIds().size());


        draft.assignConfiguration(
                new Configuration(
                        new EngineID("1"),
                        Collections.emptyList()
                )
        );
        assertEquals(new EngineID("1"), draft.getConfiguration().currentEngineId());
        assertTrue(
                draft.domainEvents()
                        .contains(new EngineAllocated(draftId, new EngineID("1")))
        );
        draft.clearDomainEvents();


        draft.assignConfiguration(
                new Configuration(
                        new EngineID("1"),
                        Arrays.asList(
                                new AttachmentID("1"),
                                new AttachmentID("2")
                        )
                )
        );
        assertEquals(new EngineID("1"), draft.getConfiguration().currentEngineId());
        assertTrue(
                draft.getConfiguration()
                        .currentAttachmentIds()
                        .containsAll(
                                List.of(
                                        new AttachmentID("1"),
                                        new AttachmentID("2")
                                )
                        )
        );
        assertEquals(2, draft.domainEvents().size());
        assertTrue(
                draft.domainEvents()
                        .containsAll(
                                List.of(
                                        new AttachmentAllocated(draftId, new AttachmentID("1")),
                                        new AttachmentAllocated(draftId, new AttachmentID("2"))
                                )
                        )
        );
        draft.clearDomainEvents();

        draft.assignConfiguration(
                new Configuration(
                        new EngineID("2"),
                        List.of(
                                new AttachmentID("2"),
                                new AttachmentID("3")
                        )
                )
        );
        assertEquals(new EngineID("2"), draft.getConfiguration().currentEngineId());
        assertTrue(
                draft.getConfiguration()
                        .currentAttachmentIds()
                        .containsAll(
                                List.of(
                                        new AttachmentID("2"),
                                        new AttachmentID("3")
                                )
                        )
        );
        assertEquals(4, draft.domainEvents().size());
        assertTrue(
                draft.domainEvents()
                        .containsAll(
                                List.of(
                                        new AttachmentDeallocated(draftId, new AttachmentID("1")),
                                        new AttachmentAllocated(draftId, new AttachmentID("3"))
                                )
                        )
        );
        draft.clearDomainEvents();

        draft.assignConfiguration(
                new Configuration(
                        null,
                        Collections.emptyList()
                )
        );
        assertNull(draft.getConfiguration().currentEngineId());
        assertTrue(
                draft.getConfiguration()
                        .currentAttachmentIds()
                        .isEmpty()
        );
        assertEquals(3, draft.domainEvents().size());
        assertTrue(
                draft.domainEvents()
                        .containsAll(
                                List.of(
                                        new AttachmentDeallocated(draftId, new AttachmentID("2")),
                                        new AttachmentDeallocated(draftId, new AttachmentID("3")),
                                        new EngineDeallocated(draftId, new EngineID("2"))
                                )
                        )
        );
    }

    private Draft buildDraft() {
        return new Draft(
                new DraftID("DraftID1"),
                "testName",
                "TestContent",
                DraftCatalog.STREAM,
                DraftType.SQL,
                "Melon"
        );
    }
}
