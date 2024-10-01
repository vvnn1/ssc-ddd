package indi.melon.ssc.context.directory.domain.tree.exception;

import indi.melon.ssc.domain.common.cqrs.DomainException;

import java.io.Serial;

/**
 * @author vvnn1
 * @since 2024/9/23 23:43
 */
public class IllegalNodeException extends DomainException {
    @Serial
    private static final long serialVersionUID = 3579918041804571426L;

    public IllegalNodeException(String message) {
        super(message);
    }
}
