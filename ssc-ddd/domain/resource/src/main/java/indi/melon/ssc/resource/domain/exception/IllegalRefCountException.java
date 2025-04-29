package indi.melon.ssc.resource.domain.exception;

import indi.melon.ssc.domain.common.cqrs.DomainException;

/**
 * @author wangmenglong
 * @since 2025/4/10 11:19
 */
public class IllegalRefCountException extends DomainException {
    public IllegalRefCountException(String message) {
        super(message);
    }
}
