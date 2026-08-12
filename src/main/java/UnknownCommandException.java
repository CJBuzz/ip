/**
 * Signals that the first word of a command is not recognised by Avon.
 */
public class UnknownCommandException extends AvonException {
    /**
     * Creates an unknown-command error with a list of valid first words.
     */
    public UnknownCommandException() {
        super("OOPS!!! I know not that command.\n"
                + "        Start with one of: todo, deadline, event, list, mark, unmark, bye.");
    }
}
