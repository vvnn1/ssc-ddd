package indi.melon.ssc.draft.north.local.appservice;

import indi.melon.ssc.common.exception.ApplicationDomainException;
import indi.melon.ssc.common.exception.ApplicationValidationException;
import indi.melon.ssc.draft.domain.draft.*;
import indi.melon.ssc.draft.domain.south.client.DraftFileTreeClient;
import indi.melon.ssc.draft.domain.template.*;
import indi.melon.ssc.draft.domain.version.Version;
import indi.melon.ssc.draft.domain.version.VersionID;
import indi.melon.ssc.draft.north.local.message.ChangeConfigurationCommand;
import indi.melon.ssc.draft.north.local.message.RenameDraftCommand;
import indi.melon.ssc.draft.north.local.message.RollbackDraftCommand;
import indi.melon.ssc.draft.south.factory.MockDraftFactory;
import indi.melon.ssc.draft.south.repository.MockDraftRepository;
import indi.melon.ssc.draft.south.repository.MockTemplateRepository;
import indi.melon.ssc.draft.south.repository.MockVersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * @author wangmenglong
 * @since 2025/5/6 11:34
 */
public class DraftAppServiceTest {
    private DraftAppService draftAppService;
    private MockDraftRepository draftRepository;
    private MockVersionRepository versionRepository;

    @BeforeEach
    public void init() {
        DraftManager draftManager = new DraftManager(
                new MockDraftFactory("mock_")
        );
        draftRepository = new MockDraftRepository();
        versionRepository = new MockVersionRepository();
        DraftFileTreeClient draftFileTreeClient = mock(DraftFileTreeClient.class);
        draftAppService = new DraftAppService(
                draftManager,
                new MockTemplateRepository(),
                draftRepository,
                draftFileTreeClient,
                versionRepository
        );
    }

    @Test
    public void should_change_configuration_normally() {
        Draft draft = new Draft(
                new DraftID("1"),
                "draft1",
                new Template(
                        new TemplateID("1"),
                        "template1",
                        "desc",
                        "content",
                        TemplateCatalog.STREAM,
                        TemplateType.SQL,
                        TemplateTag.BASE
                ),
                "melon"
        );
        draftRepository.save(draft);

        assertThrows(ApplicationValidationException.class, () ->
                draftAppService.changeConfiguration(
                        new ChangeConfigurationCommand(
                                null,
                                "1",
                                Set.of("1", "2")
                        )
                )
        );

        assertThrows(ApplicationValidationException.class, () ->
                draftAppService.changeConfiguration(
                        new ChangeConfigurationCommand(
                                "1",
                                null,
                                Set.of("1", "2")
                        )
                )
        );

        assertThrows(ApplicationValidationException.class, () ->
                draftAppService.changeConfiguration(
                        new ChangeConfigurationCommand(
                                "1",
                                "1",
                                null
                        )
                )
        );

        assertThrows(ApplicationValidationException.class, () ->
                draftAppService.changeConfiguration(
                        new ChangeConfigurationCommand(
                                "0",
                                "1",
                                Set.of("1", "2")
                        )
                )
        );

        draftAppService.changeConfiguration(
                new ChangeConfigurationCommand(
                        "1",
                        "1",
                        Set.of("1", "2")
                )
        );

        assertEquals(new EngineID("1"), draft.getConfiguration().currentEngineId());
        assertTrue(Set.of(new AttachmentID("1"), new AttachmentID("2")).containsAll(draft.getConfiguration().currentAttachmentIds()));
    }

    @Test
    public void should_rollback_draft_normally(){
        Draft draft = new Draft(
                new DraftID("1"),
                "draft1",
                new Template(
                        new TemplateID("1"),
                        "template1",
                        "desc",
                        "content",
                        TemplateCatalog.STREAM,
                        TemplateType.SQL,
                        TemplateTag.BASE
                ),
                "melon"
        );
        draftRepository.save(draft);

        Version version = new Version(
                new VersionID("1"),
                new DraftID("1"),
                "version_content",
                "-",
                "melon"
        );
        versionRepository.save(version);

        Version version2 = new Version(
                new VersionID("2"),
                new DraftID("2"),
                "version_content",
                "-",
                "melon"
        );
        versionRepository.save(version2);

        assertThrows(ApplicationValidationException.class, () -> draftAppService.rollback(
                new RollbackDraftCommand(
                        null,
                        "1",
                        "vvnn1"
                )
        ));

        assertThrows(ApplicationValidationException.class, () -> draftAppService.rollback(
                new RollbackDraftCommand(
                        "1",
                        null,
                        "vvnn1"
                )
        ));

        assertThrows(ApplicationValidationException.class, () -> draftAppService.rollback(
                new RollbackDraftCommand(
                        "1",
                        "1",
                        null
                )
        ));

        assertThrows(ApplicationValidationException.class, () -> draftAppService.rollback(
                new RollbackDraftCommand(
                        "2_not_found",
                        "1",
                        "vvnn1"
                )
        ));

        assertThrows(ApplicationValidationException.class, () -> draftAppService.rollback(
                new RollbackDraftCommand(
                        "1",
                        "3_not_found",
                        "vvnn1"
                )
        ));

        assertThrows(ApplicationDomainException.class, () -> draftAppService.rollback(
                new RollbackDraftCommand(
                        "1",
                        "2",
                        "vvnn1"
                )
        ));

        draftAppService.rollback(new RollbackDraftCommand(
                "1",
                "1",
                "vvnn12"
        ));

        assertEquals("version_content", draft.getContent());
        assertEquals("vvnn12", draft.getModifier());
    }

    @Test
    public void should_rename_draft_normally() {
        Draft draft = new Draft(
                new DraftID("1"),
                "draft1",
                new Template(
                        new TemplateID("1"),
                        "template1",
                        "desc",
                        "content",
                        TemplateCatalog.STREAM,
                        TemplateType.SQL,
                        TemplateTag.BASE
                ),
                "melon"
        );
        draftRepository.save(draft);

        assertThrows(ApplicationValidationException.class, () ->
                draftAppService.rename(
                        new RenameDraftCommand(
                                null,
                                "draft1_rename",
                                "vvnn2"
                        )
                )
        );

        assertThrows(ApplicationValidationException.class, () ->
                draftAppService.rename(
                        new RenameDraftCommand(
                                "1",
                                null,
                                "vvnn2"
                        )
                )
        );

        assertThrows(ApplicationValidationException.class, () ->
                draftAppService.rename(
                        new RenameDraftCommand(
                                "1",
                                "draft1_rename",
                                null
                        )
                )
        );

        assertThrows(ApplicationValidationException.class, () ->
                draftAppService.rename(
                        new RenameDraftCommand(
                                "2_not_found",
                                "draft1_rename",
                                "vvnn2"
                        )
                )
        );

        draftAppService.rename(
                new RenameDraftCommand(
                        "1",
                        "draft1_rename",
                        "vvnn2"
                )
        );

        assertEquals("draft1_rename", draft.getName());
        assertEquals("vvnn2", draft.getModifier());
    }
}
