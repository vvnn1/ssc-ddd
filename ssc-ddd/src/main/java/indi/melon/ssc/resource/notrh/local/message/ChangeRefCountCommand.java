package indi.melon.ssc.resource.notrh.local.message;

import indi.melon.ssc.common.exception.ApplicationValidationException;

/**
 * @author wangmenglong
 * @since 2025/4/23 13:50
 * @param id 资源id
 * @param refCount 增加/减少的引用
 */
public record ChangeRefCountCommand(Long id, Integer refCount) {
    public ChangeRefCountCommand {
        if (id == null) {
            throw new ApplicationValidationException("id is null");
        }

        if (refCount == null) {
            throw new ApplicationValidationException("refCount is null");
        }
    }
}
