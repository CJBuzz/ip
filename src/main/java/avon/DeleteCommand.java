package avon;

/**
 * Removes one task from Avon's task list.
 */
public class DeleteCommand extends Command {
    private final String fullCommand;

    /**
     * Creates a delete command whose task number is parsed during execution.
     *
     * @param fullCommand the complete command entered by the user.
     */
    public DeleteCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws AvonException {
        int taskIndex = Parser.parseTaskIndex(taskList, fullCommand, CommandType.DELETE);
        Task removedTask = taskList.removeTask(taskIndex);
        ui.showDeletedTask(removedTask, taskList.size());
        storage.save(taskList);
    }
}
