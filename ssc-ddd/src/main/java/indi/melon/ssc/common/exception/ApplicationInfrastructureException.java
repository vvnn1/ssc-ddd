package indi.melon.ssc.common.exception;

public class ApplicationInfrastructureException extends ApplicationException {
    public ApplicationInfrastructureException(String message, Exception ex) {
        super(message, ex);
    }
}