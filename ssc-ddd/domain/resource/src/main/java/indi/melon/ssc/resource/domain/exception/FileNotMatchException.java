package indi.melon.ssc.resource.domain.exception;

import indi.melon.ssc.domain.common.cqrs.DomainException;

/**
 * @author wangmenglong
 * @since 2025/4/1 19:11
 */
public class FileNotMatchException extends DomainException {
    public FileNotMatchException(String message) {
        super(message);
    }
}
