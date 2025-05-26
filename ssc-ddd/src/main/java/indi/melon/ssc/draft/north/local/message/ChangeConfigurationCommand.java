package indi.melon.ssc.draft.north.local.message;


import indi.melon.ssc.common.exception.ApplicationValidationException;

import java.util.Set;

/**
 * @author wangmenglong
 * @since 2025/5/6 11:27
 */
public record ChangeConfigurationCommand(
        String draftId,
        String engineId,
        Set<String> resourceIds
) {
    public ChangeConfigurationCommand {
        if (draftId == null || draftId.isEmpty()) {
            throw new ApplicationValidationException("id can not be null or empty");
        }

        if (engineId == null || engineId.isEmpty()) {
            throw new ApplicationValidationException("engineId can not be null or empty");
        }

        if (resourceIds == null) {
            throw new ApplicationValidationException("resourceIds can not be null");
        }
    }
}
