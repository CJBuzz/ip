package avon;

/**
 * Represents an instruction that can act on Avon's application state.
 */
public abstract class Command {
    /**
     * Executes this command.
     *
     * @param taskList the in-memory task list.
     * @param ui the command-line interface.
     * @param storage the persistent task storage.
     * @throws AvonException if the command cannot be completed.
     */
    public abstract void execute(TaskList taskList, Ui ui, Storage storage) throws AvonException;

    /**
     * Returns whether Avon should stop after executing this command.
     *
     * @return {@code true} only for an exit command.
     */
    public boolean isExit() {
        return false;
    }
}
