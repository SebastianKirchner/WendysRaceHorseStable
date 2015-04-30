package sepm.ss15.e0926076.service;

/**
 * Created by Sebastian on 14.03.2015.
 *
 * Service Exception, called to hand over Exceptions from Persistence to GUI and for invalid Service Operations
 */
public class ServiceException extends Exception {

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
