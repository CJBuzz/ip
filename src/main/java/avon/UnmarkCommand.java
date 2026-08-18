package avon;

/**
 * Marks one completed task as not completed.
 */
public class UnmarkCommand extends Command {
    private final String fullCommand;

    /**
     * Creates an unmark command whose task number is parsed during execution.
     *
     * @param fullCommand the complete command entered by the user.
     */
    public UnmarkCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws AvonException {
        int taskIndex = Parser.parseTaskIndex(taskList, fullCommand, CommandType.UNMARK);
        Task task = taskList.getTask(taskIndex);
        task.markAsNotDone();
        ui.showUnmarkedTask(task);
        storage.save(taskList);
    }
}
