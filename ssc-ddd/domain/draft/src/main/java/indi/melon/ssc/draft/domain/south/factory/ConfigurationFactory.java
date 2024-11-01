package indi.melon.ssc.draft.domain.south.factory;

import indi.melon.ssc.draft.domain.configuration.Configuration;
import indi.melon.ssc.draft.domain.draft.DraftID;

/**
 * @author wangmenglong
 * @since 2024/10/31 21:15
 */
public interface ConfigurationFactory {
    Configuration create(DraftID draftID, Configuration configuration);
}
