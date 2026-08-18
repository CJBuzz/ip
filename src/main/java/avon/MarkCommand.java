package avon;

/**
 * Marks one task as completed.
 */
public class MarkCommand extends Command {
    private final String fullCommand;

    /**
     * Creates a mark command whose task number is parsed during execution.
     *
     * @param fullCommand the complete command entered by the user
     */
    public MarkCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws AvonException {
        int taskIndex = Parser.parseTaskIndex(taskList, fullCommand, CommandType.MARK);
        Task task = taskList.getTask(taskIndex);
        task.markAsDone();
        ui.showMarkedTask(task);
        storage.save(taskList);
    }
}
