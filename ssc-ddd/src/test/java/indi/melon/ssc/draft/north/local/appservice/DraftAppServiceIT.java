package indi.melon.ssc.draft.north.local.appservice;

import indi.melon.ssc.common.exception.ApplicationDomainException;
import indi.melon.ssc.common.exception.ApplicationValidationException;
import indi.melon.ssc.draft.domain.configuration.AttachmentID;
import indi.melon.ssc.draft.domain.configuration.Configuration;
import indi.melon.ssc.draft.domain.configuration.ConfigurationID;
import indi.melon.ssc.draft.domain.configuration.EngineID;
import indi.melon.ssc.draft.domain.draft.*;
import indi.melon.ssc.draft.domain.south.client.DraftFileTreeClient;
import indi.melon.ssc.draft.domain.south.factory.ConfigurationFactory;
import indi.melon.ssc.draft.domain.south.factory.DraftFactory;
import indi.melon.ssc.draft.domain.south.repository.ConfigurationRepository;
import indi.melon.ssc.draft.domain.south.repository.DraftRepository;
import indi.melon.ssc.draft.domain.south.repository.TemplateRepository;
import indi.melon.ssc.draft.domain.south.repository.VersionRepository;
import indi.melon.ssc.draft.domain.template.*;
import indi.melon.ssc.draft.domain.version.Version;
import indi.melon.ssc.draft.domain.version.VersionID;
import indi.melon.ssc.draft.north.local.message.*;
import indi.melon.ssc.draft.south.factory.MockConfigurationFactory;
import indi.melon.ssc.draft.south.factory.MockDraftFactory;
import indi.melon.ssc.draft.south.repository.MockConfigurationRepository;
import indi.melon.ssc.draft.south.repository.MockDraftRepository;
import indi.melon.ssc.draft.south.repository.MockTemplateRepository;
import indi.melon.ssc.draft.south.repository.MockVersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author vvnn1
 * @since 2024/10/29 23:11
 */
public class DraftAppServiceIT {

    private DraftAppService draftAppService;
    private DraftManager draftManager;
    private DraftRepository draftRepository;
    private ConfigurationRepository configurationRepository;
    private DraftFileTreeClient draftFileTreeClient = mock(DraftFileTreeClient.class);
    private DraftFactory draftFactory;
    private TemplateRepository templateRepository;
    private ConfigurationFactory configurationFactory;
    private VersionRepository versionRepository;

    @BeforeEach
    public void init() {
        clearInvocations(draftFileTreeClient);
        draftRepository = new MockDraftRepository();
        configurationRepository = new MockConfigurationRepository();
        draftFactory = new MockDraftFactory("DRAFT_PREFIX_");
        templateRepository = new MockTemplateRepository();
        configurationFactory = new MockConfigurationFactory();
        versionRepository = new MockVersionRepository();

        draftManager = new DraftManager(
                draftRepository,
                configurationRepository,
                draftFileTreeClient
        );

        draftAppService =  new DraftAppService(
                draftManager,
                draftFactory,
                templateRepository,
                draftRepository,
                configurationFactory,
                configurationRepository,
                versionRepository
        );

        templateRepository.save(new Template(
                new TemplateID("1"),
                "空白的流草稿",
                "创建一个空白的流类型的作业草稿。",
                "modify the configuration",
                TemplateCatalog.STREAM,
                TemplateType.SQL,
                TemplateTag.BASE
        ));

        draftRepository.save(
                new Draft(
                        new DraftID("draftId_1"),
                        "draftDB",
                        new Template(
                                new TemplateID("1"),
                                "空白的流草稿",
                                "创建一个空白的流类型的作业草稿。",
                                "modify the configuration",
                                TemplateCatalog.STREAM,
                                TemplateType.SQL,
                                TemplateTag.BASE
                        ),
                        "vvnn1"
                )
        );

        versionRepository.save(
                new Version(
                        new VersionID("Version_1"),
                        new DraftID("draftId_1"),
                        "IamVersionContent1",
                        "remark",
                        "vvnn1"
                )
        );

        versionRepository.save(
                new Version(
                        new VersionID("Version_2"),
                        new DraftID("draftId_2"),
                        "IamVersionCtent1",
                        "remark",
                        "vvnn1"
                )
        );

        Configuration configuration = new Configuration(new DraftID("draftId_1"));
        configuration.assignEngine(new EngineID("testEngine11"));
        configuration.assignAttachments(
                Collections.singleton(
                        new AttachmentID("AttachmentID222")
                )
        );
        configurationRepository.save(configuration);
    }

    @Test
    public void should_create_draft_normally() {
        assertThrows(ApplicationValidationException.class, () -> draftAppService.create(
                new CreateDraftCommand(
                        "testDraft",
                        "templateNotFoundExceptionId",
                        "testEngineId",
                        "creator",
                        new CreateDraftCommand.Directory(
                                "directoryRoot",
                                "directoryId"
                        )
                )
        ));


        String draftId = draftAppService.create(
                new CreateDraftCommand(
                        "testDraft",
                        "1",
                        "testEngineId",
                        "creator",
                        new CreateDraftCommand.Directory(
                                "directoryRoot",
                                "directoryId"
                        )
                )
        );

        assertEquals("DRAFT_PREFIX_testDraft", draftId);


        Draft draft = draftRepository.draftOf(new DraftID("DRAFT_PREFIX_testDraft"));
        assertNotNull(draft);
        assertEquals(new DraftID("DRAFT_PREFIX_testDraft"), draft.getId());
        assertEquals("testDraft", draft.getName());
        assertEquals("modify the configuration", draft.getContent());
        assertEquals(DraftCatalog.STREAM, draft.getCatalog());
        assertEquals(DraftType.SQL, draft.getType());
        assertEquals("creator", draft.getCreator());
        assertEquals("creator", draft.getModifier());

        Configuration configuration = configurationRepository.configurationOf(new ConfigurationID("DRAFT_PREFIX_testDraft"));
        assertNotNull(configuration);
        assertEquals(new ConfigurationID("DRAFT_PREFIX_testDraft"), configuration.getId());
        assertEquals(new EngineID("testEngineId"), configuration.getEngineID());


        verify(draftFileTreeClient).create(
                new Directory("directoryId", "directoryRoot"),
                draft
        );
    }


    @Test
    public void should_save_as_normally() {
        assertThrows(ApplicationValidationException.class, () -> draftAppService.saveAs(
                new SaveDraftAsCommand(
                        "testDraft",
                        "notFoundDraftId",
                        new SaveDraftAsCommand.Directory(
                                "rootId",
                                "parentId"
                        ),
                        "vvnn1"
                )
        ));

        String draftId = draftAppService.saveAs(
                new SaveDraftAsCommand(
                        "testDraft",
                        "draftId_1",
                        new SaveDraftAsCommand.Directory(
                                "rootId",
                                "parentId"
                        ),
                        "vvnn1"
                )
        );

        assertEquals("DRAFT_PREFIX_copy_testDraft", draftId);

        Draft draft = draftRepository.draftOf(new DraftID(draftId));
        assertNotNull(draft);
        assertEquals(new DraftID("DRAFT_PREFIX_copy_testDraft"), draft.getId());
        assertEquals("testDraft", draft.getName());
        assertEquals("vvnn1", draft.getCreator());
        assertEquals("modify the configuration", draft.getContent());
        assertEquals(DraftCatalog.STREAM, draft.getCatalog());
        assertEquals(DraftType.SQL, draft.getType());

        Configuration configuration = configurationRepository.configurationOf(new ConfigurationID("DRAFT_PREFIX_copy_testDraft"));
        assertNotNull(configuration);
        assertEquals(new ConfigurationID("DRAFT_PREFIX_copy_testDraft"), configuration.getId());
        assertEquals(new EngineID("testEngine11"), configuration.getEngineID());
        assertTrue(configuration.getAttachmentIDCollection().contains(new AttachmentID("AttachmentID222")));
    }

    @Test
    public void should_save_draft_normally(){
        assertThrows(ApplicationValidationException.class, () -> draftAppService.save(
                new SaveDraftCommand(
                        "notExistDraftId",
                        "testContent",
                        "vvnn1"
                )
        ));

        draftAppService.save(
                new SaveDraftCommand(
                        "draftId_1",
                        "newContent",
                        "melon"
                )
        );

        Draft draft = draftRepository.draftOf(new DraftID("draftId_1"));
        assertNotNull(draft);
        assertEquals(new DraftID("draftId_1"), draft.getId());
        assertEquals("newContent", draft.getContent());
        assertEquals("melon", draft.getModifier());
    }

    @Test
    public void should_rollback_draft_normally(){
        assertThrows(ApplicationValidationException.class, () -> draftAppService.rollback(
                new RollbackDraftCommand(
                        "draftId_1_not_found",
                        "Version_2",
                        "vvnn1"
                )
        ));

        assertThrows(ApplicationValidationException.class, () -> draftAppService.rollback(
                new RollbackDraftCommand(
                        "draftId_1",
                        "Version_2_not_found",
                        "vvnn1"
                )
        ));

        assertThrows(ApplicationDomainException.class, () -> draftAppService.rollback(
                new RollbackDraftCommand(
                        "draftId_1",
                        "Version_2",
                        "vvnn1"
                )
        ));

        Draft draft = draftRepository.draftOf(new DraftID("draftId_1"));
        assertEquals("modify the configuration", draft.getContent());

        draftAppService.rollback(new RollbackDraftCommand(
                "draftId_1",
                "Version_1",
                "vvnn12"
        ));

        draft = draftRepository.draftOf(new DraftID("draftId_1"));
        assertEquals("IamVersionContent1", draft.getContent());
        assertEquals("vvnn12", draft.getModifier());
    }

    @Test
    public void should_rename_draft_normally() {
        Draft draft = draftRepository.draftOf(new DraftID("draftId_1"));
        assertEquals("draftDB", draft.getName());
        assertEquals("vvnn1", draft.getModifier());

        draftAppService.rename(
                new RenameDraftCommand(
                        "draftId_1",
                        "newDraftDB",
                        "vvnn2"
                )
        );

        draft = draftRepository.draftOf(new DraftID("draftId_1"));
        assertEquals("newDraftDB", draft.getName());
        assertEquals("vvnn2", draft.getModifier());
    }
}
