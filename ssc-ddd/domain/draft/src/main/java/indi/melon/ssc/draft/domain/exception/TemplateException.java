package indi.melon.ssc.draft.domain.exception;

import indi.melon.ssc.domain.common.cqrs.DomainException;

import java.io.Serial;

/**
 * @author vvnn1
 * @since 2025/2/9 20:45
 */
public class TemplateException extends DomainException {
    @Serial
    private static final long serialVersionUID = -2555353663206955754L;

    public TemplateException(String message) {
        super(message);
    }
}
