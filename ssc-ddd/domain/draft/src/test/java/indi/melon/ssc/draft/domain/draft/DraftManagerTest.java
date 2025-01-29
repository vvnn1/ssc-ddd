package indi.melon.ssc.draft.domain.draft;

import indi.melon.ssc.draft.domain.south.factory.DraftFactory;
import indi.melon.ssc.draft.domain.south.factory.MockDraftFactory;
import indi.melon.ssc.draft.domain.template.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author vvnn1
 * @since 2024/10/26 20:00
 */
public class DraftManagerTest {

    private DraftManager draftManager;

    private final DraftFactory draftFactory = new MockDraftFactory("mock_");
    @BeforeEach
    public void init() {
        draftManager = new DraftManager(
                draftFactory
        );
    }

    private Template buildTemplate() {
        return new Template(
                new TemplateID("1"),
                "mockTemplate",
                "desc",
                "content",
                TemplateCatalog.STREAM,
                TemplateType.SQL,
                TemplateTag.BASE
        );
    }

    @Test
    public void should_create_draft_by_template_normally(){
        LocalDateTime beforeCreate = LocalDateTime.now();
        Draft draft = draftManager.create(
                buildTemplate(),
                new EngineID("testEngineId"),
                "name",
                "creator"
        );
        LocalDateTime afterCreate = LocalDateTime.now();

        assertEquals(new DraftID("mock_name"), draft.getId());
        assertEquals("name", draft.getName());
        assertEquals("content", draft.getContent());
        assertEquals(DraftCatalog.STREAM, draft.getCatalog());
        assertEquals(DraftType.SQL, draft.getType());
        assertEquals("creator", draft.getCreator());
        assertEquals("creator", draft.getCreator());
        assertTrue(beforeCreate.isBefore(draft.getCreateTime()));
        assertTrue(afterCreate.isAfter(draft.getCreateTime()));
        assertTrue(beforeCreate.isBefore(draft.getUpdateTime()));
        assertTrue(afterCreate.isAfter(draft.getUpdateTime()));

        Configuration configuration = new Configuration()
                .assignEngine(new EngineID("testEngineId"))
                .assignAttachments(Collections.emptyList());
        assertEquals(configuration, draft.getConfiguration());
    }

    @Test
    public void should_create_draft_by_draft_normally() {
        Draft fromDraft = new Draft(
                new DraftID("1"),
                "draft1",
                "content",
                DraftCatalog.STREAM,
                DraftType.SQL,
                "creator"
        );


        LocalDateTime beforeCreate = LocalDateTime.now();
        Draft draft = draftManager.create(
                fromDraft,
                "draft2",
                "creator2"
        );
        LocalDateTime afterCreate = LocalDateTime.now();

        assertEquals(new DraftID("mock_copy_draft2"), draft.getId());
        assertEquals("draft2", draft.getName());
        assertEquals("content", draft.getContent());
        assertEquals(DraftCatalog.STREAM, draft.getCatalog());
        assertEquals(DraftType.SQL, draft.getType());
        assertEquals("creator2", draft.getCreator());
        assertEquals("creator2", draft.getModifier());
        assertTrue(beforeCreate.isBefore(draft.getCreateTime()));
        assertTrue(afterCreate.isAfter(draft.getCreateTime()));
        assertTrue(beforeCreate.isBefore(draft.getUpdateTime()));
        assertTrue(afterCreate.isAfter(draft.getUpdateTime()));

    }
}
