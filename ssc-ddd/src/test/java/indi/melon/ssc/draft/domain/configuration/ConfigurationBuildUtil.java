package indi.melon.ssc.draft.domain.configuration;

import indi.melon.ssc.draft.domain.configuration.AttachmentID;
import indi.melon.ssc.draft.domain.configuration.Configuration;
import indi.melon.ssc.draft.domain.configuration.ConfigurationID;
import indi.melon.ssc.draft.domain.configuration.EngineID;

import java.util.Arrays;

/**
 * @author wangmenglong
 * @since 2024/10/24 18:54
 */
public class ConfigurationBuildUtil {
    public static Configuration buildConfiguration(){
        Configuration configuration = new Configuration(
                new ConfigurationID("testID")
        );
        configuration.assignEngine(new EngineID("testEngineID"));
        configuration.assignAttachments(
                Arrays.asList(
                        new AttachmentID("testAttachmentID1"),
                        new AttachmentID("testAttachmentID2")
                )
        );

        return configuration;
    }
}
