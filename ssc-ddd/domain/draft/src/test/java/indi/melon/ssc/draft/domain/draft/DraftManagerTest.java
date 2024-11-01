package indi.melon.ssc.draft.domain.draft;

import indi.melon.ssc.draft.domain.configuration.Configuration;
import indi.melon.ssc.draft.domain.configuration.ConfigurationID;
import indi.melon.ssc.draft.domain.draft.exception.NotMatchException;
import indi.melon.ssc.draft.domain.south.client.MockDraftFileClient;
import indi.melon.ssc.draft.domain.south.repository.MockConfigurationRepository;
import indi.melon.ssc.draft.domain.south.repository.MockDraftRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static indi.melon.ssc.draft.domain.configuration.ConfigurationUtil.buildConfiguration;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vvnn1
 * @since 2024/10/26 20:00
 */
public class DraftManagerTest {
    private MockDraftFileClient draftFileClient;
    private MockDraftRepository draftRepository;

    private MockConfigurationRepository configurationRepository;

    private DraftManager draftManager;

    @BeforeEach
    public void init() {
        draftFileClient = new MockDraftFileClient();
        draftRepository = new MockDraftRepository();
        configurationRepository = new MockConfigurationRepository();
        draftManager = new DraftManager(draftRepository, configurationRepository, draftFileClient);
    }

    @Test
    public void should_create_draft_file_after_create_draft(){
        Draft draft = new Draft(
                new DraftID("DraftID"),
                "testDraftName",
                DraftCatalog.STREAM,
                "vvnn1"
        );

        Configuration notMatchConfiguration = buildConfiguration("ConfigurationID");
        assertThrows(NotMatchException.class, () -> draftManager.create(draft, notMatchConfiguration, "notMatch"));

        Configuration configuration = buildConfiguration("DraftID");
        draftManager.create(draft, configuration, "DirectoryID");

        Draft draftDB = draftRepository.draftOf(new DraftID("DraftID"));
        assertEquals(new DraftID("DraftID"), draftDB.getId());
        assertEquals("testDraftName", draftDB.getName());
        assertEquals(DraftCatalog.STREAM, draftDB.getCatalog());
        assertEquals("vvnn1", draftDB.getCreator());

        Draft draftFile = draftFileClient.getDraft();
        assertEquals(new DraftID("DraftID"), draftFile.getId());
        assertEquals("testDraftName", draftFile.getName());
        assertEquals("DirectoryID", draftFileClient.getDirectoryId());
        assertEquals("STREAM", draftFile.getCatalog().name());

        Configuration configurationDB = configurationRepository.configurationOf(new ConfigurationID("DraftID"));
        assertNotNull(configurationDB);
    }
}
