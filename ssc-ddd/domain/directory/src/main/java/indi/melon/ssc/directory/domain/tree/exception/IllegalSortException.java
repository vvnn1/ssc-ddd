package indi.melon.ssc.directory.domain.tree.exception;

import indi.melon.ssc.domain.common.cqrs.DomainException;

import java.io.Serial;

/**
 * @author wangmenglong
 * @since 2024/10/16 16:36
 */
public class IllegalSortException extends DomainException {
    @Serial
    private static final long serialVersionUID = 547908586479597710L;

    public IllegalSortException(String message) {
        super(message);
    }
}
