package indi.melon.ssc.draft.domain.draft;

import indi.melon.ssc.draft.domain.configuration.Configuration;
import indi.melon.ssc.draft.domain.draft.exception.NotMatchException;
import indi.melon.ssc.draft.domain.south.client.DraftFileTreeClient;
import indi.melon.ssc.draft.domain.south.repository.ConfigurationRepository;
import indi.melon.ssc.draft.domain.south.repository.DraftRepository;

import java.util.Objects;

/**
 * @author vvnn1
 * @since 2024/10/26 19:16
 */
public class DraftManager {
    private final DraftRepository draftRepository;
    private final ConfigurationRepository configurationRepository;
    private final DraftFileTreeClient draftFileTreeClient;

    public DraftManager(DraftRepository draftRepository, ConfigurationRepository configurationRepository, DraftFileTreeClient draftFileTreeClient) {
        this.draftRepository = draftRepository;
        this.configurationRepository = configurationRepository;
        this.draftFileTreeClient = draftFileTreeClient;
    }

    public void create(Draft draft, Configuration configuration, Directory directory) {
        if (!Objects.equals(draft.getId().getValue(), configuration.getId().getValue())) {
            throw new NotMatchException("the configuration: " + configuration.getId().getValue() + " does not match the draft: " + draft.getId().value + ".");
        }

        draftRepository.save(draft);
        configurationRepository.save(configuration);

        draftFileTreeClient.createFileNode(
                directory,
                draft
        );
    }
}
