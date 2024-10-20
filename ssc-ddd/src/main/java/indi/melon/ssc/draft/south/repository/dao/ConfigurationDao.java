package indi.melon.ssc.draft.south.repository.dao;

import indi.melon.ssc.draft.domain.configuration.Configuration;
import indi.melon.ssc.draft.domain.configuration.ConfigurationID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wangmenglong
 * @since 2024/10/22 20:03
 */
public interface ConfigurationDao extends JpaRepository<Configuration, ConfigurationID> {
}
