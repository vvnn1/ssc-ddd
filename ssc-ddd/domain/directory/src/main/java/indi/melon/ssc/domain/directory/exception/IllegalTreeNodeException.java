package indi.melon.ssc.domain.directory.exception;

import indi.melon.ssc.domain.common.cqrs.DomainException;

import java.io.Serial;

/**
 * @author vvnn1
 * @since 2024/9/23 23:43
 */
public class IllegalTreeNodeException extends DomainException {
    @Serial
    private static final long serialVersionUID = 3579918041804571426L;

    public IllegalTreeNodeException(String message) {
        super(message);
    }
}
