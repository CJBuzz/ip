/**
 * Signals that a task-number command does not identify an existing task.
 */
public class InvalidTaskNumberException extends AvonException {
    /**
     * Creates a task-number error with guidance tailored to the command.
     *
     * @param action the command action, such as mark, unmark, or delete
     * @param problem a clear explanation of the invalid number
     */
    public InvalidTaskNumberException(String action, String problem) {
        this(action, problem, true);
    }

    /**
     * Creates a task-number error, optionally with a valid command example.
     *
     * @param action the command action, such as mark, unmark, or delete
     * @param problem a clear explanation of the invalid number
     * @param shouldShowExample whether a command example would help resolve the error
     */
    public InvalidTaskNumberException(String action, String problem, boolean shouldShowExample) {
        super("Pardon, I beseech thee! I cannot " + action + " that task.\n"
                + "        " + problem
                + (shouldShowExample ? "\n        Example: " + action + " 1" : ""));
    }
}
