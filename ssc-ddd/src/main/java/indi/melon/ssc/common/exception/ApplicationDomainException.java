package indi.melon.ssc.common.exception;

public class ApplicationDomainException extends ApplicationException {
    public ApplicationDomainException(String message, Exception ex) {
        super(message, ex);
    }
}