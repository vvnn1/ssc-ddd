package indi.melon.ssc.draft.domain.configuration;

/**
 * @author vvnn1
 * @since 2024/10/28 23:19
 */
public class ConfigurationUtil {
    public static Configuration buildConfiguration(String id){
        return new Configuration(new ConfigurationID(id));
    }
}
