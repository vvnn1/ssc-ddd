package indi.melon.ssc.draft.south.factory;

import indi.melon.ssc.draft.domain.configuration.Configuration;
import indi.melon.ssc.draft.domain.draft.DraftID;
import indi.melon.ssc.draft.domain.south.factory.ConfigurationFactory;

/**
 * @author wangmenglong
 * @since 2024/11/1 17:40
 */
public class MockConfigurationFactory implements ConfigurationFactory {
    @Override
    public Configuration create(DraftID draftID, Configuration configuration) {
        Configuration configurationCopy = new Configuration(draftID);
        configurationCopy.assignEngine(
                configuration.getEngineID()
        );
        configurationCopy.assignAttachments(
                configuration.getAttachmentIDCollection()
        );
        return configurationCopy;
    }
}
