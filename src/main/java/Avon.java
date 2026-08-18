import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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
                CommandType commandType = CommandType.parse(command);
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
        Task task = parseTask(command, commandType);
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
        int taskIndex = parseTaskIndex(taskList, command, CommandType.MARK);
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
        int taskIndex = parseTaskIndex(taskList, command, CommandType.UNMARK);
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
        int taskIndex = parseTaskIndex(taskList, command, CommandType.DELETE);
        Task removedTask = taskList.removeTask(taskIndex);
        ui.showDeletedTask(removedTask, taskList.size());
    }

    /**
     * Parses and validates the one-based task number in a task-number command.
     *
     * @param taskList the in-memory task list
     * @param command the complete command entered by the user
     * @param commandType the type of task-number command
     * @return the corresponding zero-based task index
     * @throws InvalidTaskNumberException if the number is missing, malformed, or out of range
     */
    private static int parseTaskIndex(TaskList taskList, String command, CommandType commandType)
            throws InvalidTaskNumberException {
        String keyword = commandType.getKeyword();
        String taskNumberText = command.substring(keyword.length()).trim();
        if (taskNumberText.isEmpty()) {
            throw new InvalidTaskNumberException(commandType,
                    "Add a task number after '" + keyword + "'.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(taskNumberText);
        } catch (NumberFormatException exception) {
            throw new InvalidTaskNumberException(commandType,
                    "Use one whole task number greater than zero.");
        }

        if (taskList.size() == 0) {
            throw new InvalidTaskNumberException(commandType,
                    "Thy task list is empty; add a task first.", false);
        }
        if (taskNumber <= 0 || taskNumber > taskList.size()) {
            throw new InvalidTaskNumberException(commandType,
                    "Choose a task number from 1 to " + taskList.size() + ".");
        }
        return taskNumber - 1;
    }

    /**
     * Creates the appropriate task subtype for a command.
     *
     * @param command the command entered by the user
     * @param commandType the type of task to create
     * @return the task represented by the command
     */
    private static Task parseTask(String command, CommandType commandType) throws AvonException {
        switch (commandType) {
        case TODO:
            String todoKeyword = CommandType.TODO.getKeyword();
            return new Todo(extractDescription(command, todoKeyword, "todo DESCRIPTION"));
        case DEADLINE:
            return parseDeadline(command);
        case EVENT:
            return parseEvent(command);
        default:
            throw new IllegalArgumentException("Command does not create a task.");
        }
    }

    /**
     * Extracts a command's description after its keyword.
     *
     * @param command the command entered by the user
     * @param keyword the command keyword to remove
     * @return the non-empty task description
     */
    private static String extractDescription(String command, String keyword, String example)
            throws EmptyDescriptionException {
        String description = command.substring(keyword.length()).trim();
        if (description.isEmpty()) {
            throw new EmptyDescriptionException(keyword, example);
        }
        return description;
    }

    /**
     * Parses a deadline command into a Deadline task.
     *
     * @param command the deadline command entered by the user
     * @return the parsed deadline task
     */
    private static Deadline parseDeadline(String command) throws AvonException {
        String deadlineKeyword = CommandType.DEADLINE.getKeyword();
        String example = "deadline DESCRIPTION /by yyyy-MM-dd";
        String details = extractDescription(command, deadlineKeyword,
                example);
        int byIndex = details.indexOf("/by");
        if (byIndex < 0) {
            throw new InvalidTaskFormatException(deadlineKeyword,
                    "Include '/by' before the deadline date or time.", example);
        }
        if (details.indexOf("/by", byIndex + 3) >= 0) {
            throw new InvalidTaskFormatException(deadlineKeyword,
                    "Use '/by' exactly once.", example);
        }
        if (byIndex == 0) {
            throw new EmptyDescriptionException(deadlineKeyword, example);
        }

        String description = details.substring(0, byIndex).trim();
        String by = requireTaskDetail(details.substring(byIndex + 3).trim(),
                deadlineKeyword, "Add a date or time after '/by'.", example);
        try {
            return new Deadline(description, LocalDate.parse(by));
        } catch (DateTimeParseException exception) {
            throw new InvalidTaskFormatException(deadlineKeyword,
                    "Use a real date in yyyy-MM-dd format.", example);
        }
    }

    /**
     * Parses an event command into an Event task.
     *
     * @param command the event command entered by the user
     * @return the parsed event task
     */
    private static Event parseEvent(String command) throws AvonException {
        String eventKeyword = CommandType.EVENT.getKeyword();
        String example = "event DESCRIPTION /from START /to END";
        String details = extractDescription(command, eventKeyword,
                example);
        int fromIndex = details.indexOf("/from");
        int toIndex = details.indexOf("/to");
        if (fromIndex < 0) {
            throw new InvalidTaskFormatException(eventKeyword,
                    "Include '/from' before the start date or time.", example);
        }
        if (toIndex < 0) {
            throw new InvalidTaskFormatException(eventKeyword,
                    "Include '/to' before the end date or time.", example);
        }
        if (details.indexOf("/from", fromIndex + 5) >= 0
                || details.indexOf("/to", toIndex + 3) >= 0) {
            throw new InvalidTaskFormatException(eventKeyword,
                    "Use '/from' and '/to' exactly once each.", example);
        }
        if (toIndex < fromIndex) {
            throw new InvalidTaskFormatException(eventKeyword,
                    "Place '/from' before '/to'.", example);
        }
        if (fromIndex == 0) {
            throw new EmptyDescriptionException(eventKeyword, example);
        }

        String description = details.substring(0, fromIndex).trim();
        String from = requireTaskDetail(details.substring(fromIndex + 5, toIndex).trim(),
                eventKeyword, "Add a start date or time after '/from'.", example);
        String to = requireTaskDetail(details.substring(toIndex + 3).trim(),
                eventKeyword, "Add an end date or time after '/to'.", example);
        return new Event(description, from, to);
    }

    /**
     * Ensures that a parsed command component is not empty.
     *
     * @param value the component to validate
     * @param taskType the type of task being entered
     * @param problem the explanation shown if the component is empty
     * @param example a complete example of the expected command
     * @return the original non-empty component
     * @throws InvalidTaskFormatException if the component is empty
     */
    private static String requireTaskDetail(String value, String taskType, String problem,
            String example) throws InvalidTaskFormatException {
        if (value.isEmpty()) {
            throw new InvalidTaskFormatException(taskType, problem, example);
        }
        return value;
    }

}
