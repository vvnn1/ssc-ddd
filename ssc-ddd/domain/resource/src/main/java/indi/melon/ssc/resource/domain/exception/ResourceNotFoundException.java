package indi.melon.ssc.resource.domain.exception;

import indi.melon.ssc.domain.common.cqrs.DomainException;

/**
 * @author wangmenglong
 * @since 2025/4/3 14:19
 */
public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
