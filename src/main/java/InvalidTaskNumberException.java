/**
 * Signals that a task-number command does not identify an existing task.
 */
public class InvalidTaskNumberException extends AvonException {
    /**
     * Creates a task-number error with guidance tailored to the command.
     *
     * @param commandType the type of task-number command
     * @param problem a clear explanation of the invalid number
     */
    public InvalidTaskNumberException(CommandType commandType, String problem) {
        this(commandType, problem, true);
    }

    /**
     * Creates a task-number error, optionally with a valid command example.
     *
     * @param commandType the type of task-number command
     * @param problem a clear explanation of the invalid number
     * @param shouldShowExample whether a command example would help resolve the error
     */
    public InvalidTaskNumberException(CommandType commandType, String problem,
            boolean shouldShowExample) {
        super("I cannot " + commandType.getKeyword() + " that task.\n"
                + problem
                + (shouldShowExample
                    ? "\nExample: " + commandType.getKeyword() + " 1" : ""));
    }
}
