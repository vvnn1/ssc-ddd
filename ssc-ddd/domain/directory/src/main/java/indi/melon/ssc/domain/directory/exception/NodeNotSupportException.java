package indi.melon.ssc.domain.directory.exception;

import indi.melon.ssc.domain.common.cqrs.DomainException;

/**
 * @author vvnn1
 * @since 2024/9/25 21:07
 */
public class NodeNotSupportException extends DomainException {
    public NodeNotSupportException(String message) {
        super(message);
    }
}
