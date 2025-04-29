package indi.melon.ssc.resource.notrh.local.message;

import indi.melon.ssc.common.exception.ApplicationValidationException;
import jakarta.annotation.Nonnull;

/**
 * @author wangmenglong
 * @since 2025/4/19 16:16
 * @param id 资源id
 * @param newName 新资源名称
 */
public record RenameResourceCommand(Long id, String newName) {
    public RenameResourceCommand {
        if (id == null) {
            throw new ApplicationValidationException("id can not be null");
        }

        if (newName == null || newName.isEmpty()) {
            throw new ApplicationValidationException("newName can not be null or empty");
        }
    }
}
