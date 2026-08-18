package avon;

/**
 * Adds a parsed task to Avon's task list.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command for the task to add.
     *
     * @param task the parsed task.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws StorageException {
        taskList.add(task);
        storage.save(taskList);
        ui.showAddedTask(task, taskList.size());
    }
}
