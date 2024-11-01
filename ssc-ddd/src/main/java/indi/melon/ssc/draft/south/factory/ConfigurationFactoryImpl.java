package indi.melon.ssc.draft.south.factory;

import indi.melon.ssc.draft.domain.configuration.Configuration;
import indi.melon.ssc.draft.domain.draft.DraftID;
import indi.melon.ssc.draft.domain.south.factory.ConfigurationFactory;
import org.springframework.stereotype.Component;

/**
 * @author wangmenglong
 * @since 2024/11/1 17:37
 */
@Component
public class ConfigurationFactoryImpl implements ConfigurationFactory {
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
