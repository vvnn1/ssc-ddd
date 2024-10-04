package indi.melon.ssc.draft.context.domain.version.exception;

import indi.melon.ssc.domain.common.cqrs.DomainException;

import java.io.Serial;

/**
 * @author vvnn1
 * @since 2024/10/2 22:01
 */
public class VersionLockedException extends DomainException {
    @Serial
    private static final long serialVersionUID = -5784012597242541401L;

    public VersionLockedException(String message) {
        super(message);
    }
}
