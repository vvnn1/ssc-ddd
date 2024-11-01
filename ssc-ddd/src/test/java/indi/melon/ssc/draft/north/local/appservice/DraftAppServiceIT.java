package indi.melon.ssc.draft.north.local.appservice;

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
import indi.melon.ssc.draft.domain.template.*;
import indi.melon.ssc.draft.north.local.message.SaveAsCommand;
import indi.melon.ssc.draft.north.local.message.CreateDraftCommand;
import indi.melon.ssc.draft.south.client.MockDraftFileClient;
import indi.melon.ssc.draft.south.factory.MockConfigurationFactory;
import indi.melon.ssc.draft.south.factory.MockDraftFactory;
import indi.melon.ssc.draft.south.repository.MockConfigurationRepository;
import indi.melon.ssc.draft.south.repository.MockDraftRepository;
import indi.melon.ssc.draft.south.repository.MockTemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vvnn1
 * @since 2024/10/29 23:11
 */
public class DraftAppServiceIT {

    private DraftAppService draftAppService;
    private DraftManager draftManager;
    private DraftRepository draftRepository;
    private ConfigurationRepository configurationRepository;
    private DraftFileTreeClient draftFileTreeClient;
    private DraftFactory draftFactory;
    private TemplateRepository templateRepository;
    private ConfigurationFactory configurationFactory;

    @BeforeEach
    public void init() {
        draftRepository = new MockDraftRepository();
        configurationRepository = new MockConfigurationRepository();
        draftFileTreeClient = new MockDraftFileClient();
        draftFactory = new MockDraftFactory("DRAFT_PREFIX_");
        templateRepository = new MockTemplateRepository();
        configurationFactory = new MockConfigurationFactory();

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
                configurationRepository
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


        MockDraftFileClient mockDraftFileClient = (MockDraftFileClient) draftFileTreeClient;
        assertEquals(draft, mockDraftFileClient.getDraft());
        assertEquals("directoryId", mockDraftFileClient.getDirectory().id());
        assertEquals("directoryRoot", mockDraftFileClient.getDirectory().rootId());
    }


    @Test
    public void should_save_as_normally() {
        assertThrows(ApplicationValidationException.class, () -> draftAppService.saveAs(
                new SaveAsCommand(
                        "testDraft",
                        "notFoundDraftId",
                        new SaveAsCommand.Directory(
                                "rootId",
                                "parentId"
                        ),
                        "vvnn1"
                )
        ));

        String draftId = draftAppService.saveAs(
                new SaveAsCommand(
                        "testDraft",
                        "draftId_1",
                        new SaveAsCommand.Directory(
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
}
