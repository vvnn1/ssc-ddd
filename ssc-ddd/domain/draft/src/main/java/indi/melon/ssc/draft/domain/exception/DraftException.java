package indi.melon.ssc.draft.domain.exception;

import indi.melon.ssc.domain.common.cqrs.DomainException;

import java.io.Serial;

/**
 * @author vvnn1
 * @since 2025/2/9 20:43
 */
public class DraftException extends DomainException {
    @Serial
    private static final long serialVersionUID = -8512118170006873236L;

    public DraftException(String message) {
        super(message);
    }
}
