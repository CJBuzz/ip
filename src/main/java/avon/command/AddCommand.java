package avon.command;

import avon.exception.StorageException;
import avon.storage.Storage;
import avon.task.Task;
import avon.task.TaskList;
import avon.ui.Ui;

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
        try {
            storage.save(taskList);
        } catch (StorageException exception) {
            taskList.removeTask(taskList.size() - 1);
            throw exception;
        }
        ui.showAddedTask(task, taskList.size());
    }
}
