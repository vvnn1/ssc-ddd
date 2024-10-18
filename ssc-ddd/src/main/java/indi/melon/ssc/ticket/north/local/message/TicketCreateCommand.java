package indi.melon.ssc.ticket.north.local.message;

import indi.melon.ssc.common.exception.ApplicationValidationException;
import lombok.Getter;

/**
 * @author wangmenglong
 * @since 2024/10/17 19:40
 */
@Getter
public class TicketCreateCommand<T> {
    private final String bizTag;
    private final Class<T> clazz;

    private TicketCreateCommand(String bizTag, Class<T> clazz) {
        this.bizTag = bizTag;
        this.clazz = clazz;
    }

    public static <T> TicketCreateCommand<T> of(String bizTag, Class<T> clazz) {
        if (bizTag == null) {
            throw new ApplicationValidationException("bizTag can not be null");
        }

        if (clazz == null) {
            throw new ApplicationValidationException("clazz can not be null");
        }

        return new TicketCreateCommand<>(bizTag, clazz);
    }
}
