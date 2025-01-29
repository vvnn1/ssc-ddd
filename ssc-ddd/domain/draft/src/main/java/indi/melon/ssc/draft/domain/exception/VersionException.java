package indi.melon.ssc.draft.domain.exception;

import indi.melon.ssc.domain.common.cqrs.DomainException;

import java.io.Serial;

/**
 * @author vvnn1
 * @since 2025/2/9 20:44
 */
public class VersionException extends DomainException {
    @Serial
    private static final long serialVersionUID = -5795878984297477907L;

    public VersionException(String message) {
        super(message);
    }
}
