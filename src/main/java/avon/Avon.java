package avon;

import java.nio.file.Path;

/**
 * Runs the command-line interface for Avon.
 */
public class Avon {
    private static final Path DATA_FILE_PATH = Path.of("data", "avon.txt");

    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();
        Storage storage = new Storage(DATA_FILE_PATH);
        TaskList taskList = loadTasks(storage, ui);
        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            ui.showSeparator();

            try {
                CommandType commandType = Parser.parseCommandType(command);
                if (commandType == CommandType.BYE) {
                    ui.showGoodbye();
                    ui.showSeparator();
                    break;
                }
                executeCommand(taskList, storage, ui, command, commandType);
            } catch (AvonException exception) {
                ui.showError(exception);
            }
            ui.showSeparator();
        }
        ui.close();
    }

    /**
     * Executes a non-exit command after identifying its first word.
     *
     * @param taskList the in-memory task list
     * @param storage the persistent task storage
     * @param ui the command-line interface
     * @param command the command entered by the user
     * @param commandType the identified type of the command
     * @throws AvonException if the command cannot be understood or completed
     * @throws IllegalArgumentException if the switch-case block reaches a state 
     *  it is not meant to reach (for future debugging)
     */
    private static void executeCommand(TaskList taskList, Storage storage, Ui ui,
            String command, CommandType commandType) throws AvonException {
        switch (commandType) {
        case LIST:
            ui.showTaskList(taskList);
            break;
        case MARK:
            markTask(taskList, ui, command);
            storage.save(taskList);
            break;
        case UNMARK:
            unmarkTask(taskList, ui, command);
            storage.save(taskList);
            break;
        case DELETE:
            deleteTask(taskList, ui, command);
            storage.save(taskList);
            break;
        case TODO:
        case DEADLINE:
        case EVENT:
            addTask(taskList, storage, ui, command, commandType);
            break;
        case BYE:
            throw new IllegalArgumentException("Bye must be handled before command execution.");
        default:
            throw new IllegalArgumentException("Unsupported command type.");
        }
    }

    /**
     * Adds a command to the task list and acknowledges the addition.
     *
     * @param taskList the in-memory task list
     * @param storage the persistent task storage
     * @param ui the command-line interface
     * @param command the text entered by the user
     * @param commandType the type of task to create
     */
    private static void addTask(TaskList taskList, Storage storage, Ui ui,
            String command, CommandType commandType) throws AvonException {
        Task task = Parser.parseTask(command, commandType);
        taskList.add(task);
        storage.save(taskList);
        ui.showAddedTask(task, taskList.size());
    }

    /**
     * Loads saved tasks, falling back to an empty list if loading fails.
     *
     * @param storage the persistent task storage
     * @param ui the command-line interface
     * @return the restored task list
     */
    private static TaskList loadTasks(Storage storage, Ui ui) {
        try {
            return new TaskList(storage.load());
        } catch (StorageException exception) {
            ui.showError(exception);
            return new TaskList();
        }
    }

    /**
     * Marks the task identified by the one-based number in a mark command as done.
     *
     * @param taskList the in-memory task list
     * @param ui the command-line interface
     * @param command the mark command entered by the user
     */
    private static void markTask(TaskList taskList, Ui ui, String command)
            throws InvalidTaskNumberException {
        int taskIndex = Parser.parseTaskIndex(taskList, command, CommandType.MARK);
        Task task = taskList.getTask(taskIndex);
        task.markAsDone();
        ui.showMarkedTask(task);
    }

    /**
     * Marks the task identified by the one-based number in an unmark command as not done.
     *
     * @param taskList the in-memory task list
     * @param ui the command-line interface
     * @param command the unmark command entered by the user
     */
    private static void unmarkTask(TaskList taskList, Ui ui, String command)
            throws InvalidTaskNumberException {
        int taskIndex = Parser.parseTaskIndex(taskList, command, CommandType.UNMARK);
        Task task = taskList.getTask(taskIndex);
        task.markAsNotDone();
        ui.showUnmarkedTask(task);
    }

    /**
     * Removes the task identified by the one-based number in a delete command.
     *
     * @param taskList the in-memory task list
     * @param ui the command-line interface
     * @param command the delete command entered by the user
     * @throws InvalidTaskNumberException if the command does not identify an existing task
     */
    private static void deleteTask(TaskList taskList, Ui ui, String command)
            throws InvalidTaskNumberException {
        int taskIndex = Parser.parseTaskIndex(taskList, command, CommandType.DELETE);
        Task removedTask = taskList.removeTask(taskIndex);
        ui.showDeletedTask(removedTask, taskList.size());
    }

}
