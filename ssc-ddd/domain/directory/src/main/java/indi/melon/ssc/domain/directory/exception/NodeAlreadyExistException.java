package indi.melon.ssc.domain.directory.exception;

import indi.melon.ssc.domain.common.cqrs.DomainException;

import java.io.Serial;

/**
 * @author vvnn1
 * @since 2024/9/23 19:36
 */
public class NodeAlreadyExistException extends DomainException {
    @Serial
    private static final long serialVersionUID = -8785443002262770659L;

    public NodeAlreadyExistException(String message) {
        super(message);
    }
}
