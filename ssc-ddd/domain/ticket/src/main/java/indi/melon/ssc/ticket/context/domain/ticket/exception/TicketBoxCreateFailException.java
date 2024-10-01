package indi.melon.ssc.ticket.context.domain.ticket.exception;

import indi.melon.ssc.domain.common.cqrs.DomainException;

import java.io.Serial;

/**
 * @author vvnn1
 * @since 2024/9/22 20:23
 */
public class TicketBoxCreateFailException extends DomainException {

    @Serial
    private static final long serialVersionUID = 872548024029165695L;

    public TicketBoxCreateFailException(Throwable cause) {
        super(cause);
    }
}
