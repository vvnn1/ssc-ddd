package indi.melon.ssc.resource.domain.exception;

import indi.melon.ssc.domain.common.cqrs.DomainException;

/**
 * @author wangmenglong
 * @since 2025/4/3 14:20
 */
public class FileNotFoundException extends DomainException {
    public FileNotFoundException(String message) {
        super(message);
    }
}
