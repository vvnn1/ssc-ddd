package indi.melon.ssc.directory.domain.tree.exception;

import indi.melon.ssc.domain.common.cqrs.DomainException;

/**
 * @author vvnn1
 * @since 2024/9/25 21:07
 */
public class NotSupportException extends DomainException {
    public NotSupportException(String message) {
        super(message);
    }
}
