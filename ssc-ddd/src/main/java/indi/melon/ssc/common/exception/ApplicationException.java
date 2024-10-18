package indi.melon.ssc.common.exception;

import java.io.Serial;

/**
 * @author vvnn1
 * @since 2024/10/5 22:02
 */
public abstract class ApplicationException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -8521794214035312808L;

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
