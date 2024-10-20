package indi.melon.ssc.draft.domain.south.repository;

import indi.melon.ssc.draft.domain.configuration.Configuration;
import indi.melon.ssc.draft.domain.configuration.ConfigurationID;

/**
 * @author wangmenglong
 * @since 2024/10/22 20:00
 */
public interface ConfigurationRepository {
    Configuration configurationOf(ConfigurationID configurationID);
    void save(Configuration configuration);
}
