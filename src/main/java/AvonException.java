/**
 * Represents an error caused by a command that Avon cannot carry out.
 */
public class AvonException extends Exception {
    /**
     * Creates an Avon exception with guidance for the user.
     *
     * @param message the explanation shown to the user
     */
    public AvonException(String message) {
        super(message);
    }
}
