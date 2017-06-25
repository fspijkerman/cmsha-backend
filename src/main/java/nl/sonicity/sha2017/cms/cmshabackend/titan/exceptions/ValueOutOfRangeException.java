package nl.sonicity.sha2017.cms.cmshabackend.titan.exceptions;

/**
 * Created by hugo on 24/06/2017.
 */
public class ValueOutOfRangeException extends Exception {
    public ValueOutOfRangeException() {
        super();
    }

    public ValueOutOfRangeException(String message) {
        super(message);
    }

    public ValueOutOfRangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValueOutOfRangeException(Throwable cause) {
        super(cause);
    }
}
