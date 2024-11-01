package indi.melon.ssc.draft.south.repository;

import indi.melon.ssc.draft.domain.configuration.Configuration;
import indi.melon.ssc.draft.domain.configuration.ConfigurationID;
import indi.melon.ssc.draft.domain.south.repository.ConfigurationRepository;
import indi.melon.ssc.draft.south.repository.dao.ConfigurationDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import static indi.melon.ssc.draft.domain.configuration.ConfigurationBuildUtil.buildConfiguration;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author wangmenglong
 * @since 2024/10/22 19:59
 */
@DataJpaTest(
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {ConfigurationRepository.class, ConfigurationDao.class}
                )
        }
)
public class ConfigurationRepositoryIT {
    @Autowired
    private ConfigurationRepository configurationRepository;

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void should_save_and_get_same_configuration() {
        Configuration configuration = buildConfiguration();
        configurationRepository.save(configuration);
        Configuration configurationDB = configurationRepository.configurationOf(new ConfigurationID("testID"));

        assertEquals(configuration.getId(), configurationDB.getId());
        assertEquals(configuration.getEngineID(), configurationDB.getEngineID());
        assertEquals(configuration.getAttachmentIDCollection().size(), configurationDB.getAttachmentIDCollection().size());
        assertArrayEquals(
                configuration.getAttachmentIDCollection().toArray(),
                configurationDB.getAttachmentIDCollection().toArray()
        );
    }
}
