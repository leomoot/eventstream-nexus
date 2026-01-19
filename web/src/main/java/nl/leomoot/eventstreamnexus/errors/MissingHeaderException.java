package nl.leomoot.eventstreamnexus.errors;

/**
 * Exception indicating a missing required header.
 */
public class MissingHeaderException extends RuntimeException {

    /**
     * @param headerName missing header key
     */
    public MissingHeaderException(String headerName) {
        super("Required header '" + headerName + "' is missing");
    }
}
