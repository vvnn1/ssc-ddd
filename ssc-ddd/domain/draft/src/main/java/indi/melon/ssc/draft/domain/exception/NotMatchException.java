package indi.melon.ssc.draft.domain.exception;

import indi.melon.ssc.domain.common.cqrs.DomainException;

import java.io.Serial;

/**
 * @author vvnn1
 * @since 2024/10/26 0:38
 */
public class NotMatchException extends DomainException {
    @Serial
    private static final long serialVersionUID = -8741397562380295996L;

    public NotMatchException(String message) {
        super(message);
    }
}
