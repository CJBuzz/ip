/**
 * Signals that a mark or unmark command does not identify an existing task.
 */
public class InvalidTaskNumberException extends AvonException {
    /**
     * Creates a task-number error with guidance tailored to the command.
     *
     * @param action the command action, such as mark or unmark
     * @param problem a clear explanation of the invalid number
     */
    public InvalidTaskNumberException(String action, String problem) {
        super("OOPS!!! I cannot " + action + " that task.\n"
                + "        " + problem + "\n"
                + "        Example: " + action + " 1");
    }
}
