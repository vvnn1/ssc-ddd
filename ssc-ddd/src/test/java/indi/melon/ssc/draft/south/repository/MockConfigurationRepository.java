package indi.melon.ssc.draft.south.repository;

import indi.melon.ssc.draft.domain.configuration.Configuration;
import indi.melon.ssc.draft.domain.configuration.ConfigurationID;
import indi.melon.ssc.draft.domain.south.repository.ConfigurationRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vvnn1
 * @since 2024/10/30 23:58
 */
public class MockConfigurationRepository implements ConfigurationRepository {
    private final Map<ConfigurationID, Configuration> db = new HashMap<>();
    @Override
    public Configuration configurationOf(ConfigurationID id) {
        return db.get(id);
    }

    @Override
    public void save(Configuration configuration) {
        db.put(configuration.getId(), configuration);
    }

    @Override
    public void delete(ConfigurationID id) {
        db.remove(id);
    }
}
