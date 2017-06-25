package nl.sonicity.sha2017.cms.cmshabackend.titan.exceptions;

/**
 * Created by hugo on 24/06/2017.
 */
public class RequestFailedException extends RuntimeException {
    public RequestFailedException() {
        super();
    }

    public RequestFailedException(String message) {
        super(message);
    }

    public RequestFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestFailedException(Throwable cause) {
        super(cause);
    }
}
