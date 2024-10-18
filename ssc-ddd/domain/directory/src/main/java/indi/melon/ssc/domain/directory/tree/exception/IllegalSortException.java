package indi.melon.ssc.domain.directory.tree.exception;

import indi.melon.ssc.domain.common.cqrs.DomainException;

/**
 * @author wangmenglong
 * @since 2024/10/16 16:36
 */
public class IllegalSortException extends DomainException {
    public IllegalSortException(String message) {
        super(message);
    }
}
