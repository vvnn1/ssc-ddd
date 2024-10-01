package indi.melon.ssc.domain.directory.exception;

import indi.melon.ssc.domain.common.cqrs.DomainException;

import java.io.Serial;

/**
 * @author vvnn1
 * @since 2024/9/29 19:13
 */
public class NotFoundException extends DomainException {
    @Serial
    private static final long serialVersionUID = -3178849163421622245L;

    public NotFoundException(String message) {
        super(message);
    }
}
