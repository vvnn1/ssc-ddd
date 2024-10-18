package indi.melon.ssc.ticket.north.local.message;

import indi.melon.ssc.common.exception.ApplicationValidationException;

/**
 * @author wangmenglong
 * @since 2024/10/17 19:40
 */
public record TicketCreateCommand<T>(String bizTag,
                                     Class<T> clazz) {
    public TicketCreateCommand {
        if (bizTag == null) {
            throw new ApplicationValidationException("bizTag can not be null");
        }

        if (clazz == null) {
            throw new ApplicationValidationException("clazz can not be null");
        }
    }
}
