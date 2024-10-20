package indi.melon.ssc.draft.south.repository;

import indi.melon.ssc.common.exception.ApplicationInfrastructureException;
import indi.melon.ssc.draft.domain.configuration.Configuration;
import indi.melon.ssc.draft.domain.configuration.ConfigurationID;
import indi.melon.ssc.draft.domain.south.repository.ConfigurationRepository;
import indi.melon.ssc.draft.south.repository.dao.ConfigurationDao;
import org.springframework.stereotype.Repository;

/**
 * @author wangmenglong
 * @since 2024/10/22 20:00
 */
@Repository
public class ConfigurationRepositoryImpl implements ConfigurationRepository {
    private final ConfigurationDao configurationDao;

    public ConfigurationRepositoryImpl(ConfigurationDao configurationDao) {
        this.configurationDao = configurationDao;
    }

    @Override
    public Configuration configurationOf(ConfigurationID id) {
        try {
            return configurationDao.findById(id).orElse(null);
        } catch (Exception e) {
            throw new ApplicationInfrastructureException("find configuration by " + id + " failed.", e);
        }
    }

    @Override
    public void save(Configuration configuration) {
        try {
            configurationDao.save(configuration);
        } catch (Exception e){
            throw new ApplicationInfrastructureException("save configuration failed. configuration: " + configuration, e);
        }
    }
}
