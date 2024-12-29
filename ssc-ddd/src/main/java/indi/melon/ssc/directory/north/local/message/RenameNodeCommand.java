package indi.melon.ssc.directory.north.local.message;

import indi.melon.ssc.common.exception.ApplicationValidationException;
import indi.melon.ssc.domain.common.utils.StringUtils;
import jakarta.annotation.Nonnull;

/**
 * @author wangmenglong
 * @since 2024/10/18 16:25
 */
public record RenameNodeCommand(@Nonnull String id, @Nonnull String newName) {
    public RenameNodeCommand {
        if (StringUtils.isBlank(newName)) {
            throw new ApplicationValidationException("newName should not be blank.");
        }
    }
}
