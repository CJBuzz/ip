import java.util.Scanner;

/**
 * Runs the command-line interface for Avon.
 */
public class Avon {
    private static final String SEPARATOR = "____________________________________________________________";
    private static final String AVON_PREFIX = "Avon:\t";

    public static void main(String[] args) {
        String banner = """
                ___                    
               /   |_   ______  ____  
              / /| | | / / __ \\/ __ \\ 
             / ___ | |/ / /_/ / / / / 
            /_/  |_|___/\\____/_/ /_/ """;
        System.out.println(SEPARATOR);
        System.out.println(banner);
        System.out.println(AVON_PREFIX + "Hark! I am Avon who stands before thee.");
        System.out.println(AVON_PREFIX + "How may my hand or wit now serve thy need?");
        System.out.println(SEPARATOR);

        Scanner scanner = new Scanner(System.in);
        TaskList taskList = new TaskList();
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(SEPARATOR);

            try {
                if (command.equals("bye")) {
                    System.out.println(AVON_PREFIX + "Fare thee well! Pray heavens our paths cross anon.");
                    System.out.println(SEPARATOR);
                    break;
                }
                executeCommand(taskList, command);
            } catch (AvonException exception) {
                System.out.println(AVON_PREFIX + exception.getMessage());
            }
            System.out.println(SEPARATOR);
        }
        scanner.close();
    }

    /**
     * Executes a non-exit command after identifying its first word.
     *
     * @param taskList the in-memory task list
     * @param command the command entered by the user
     * @throws AvonException if the command cannot be understood or completed
     */
    private static void executeCommand(TaskList taskList, String command) throws AvonException {
        if (command.equals("list")) {
            printTasks(taskList);
        } else if (command.equals("mark") || command.startsWith("mark ")) {
            markTask(taskList, command);
        } else if (command.equals("unmark") || command.startsWith("unmark ")) {
            unmarkTask(taskList, command);
        } else if (isTaskCommand(command)) {
            addTask(taskList, command);
        } else {
            throw new UnknownCommandException();
        }
    }

    /**
     * Checks whether a command begins with a supported task keyword.
     *
     * @param command the command entered by the user
     * @return true if the command is a todo, deadline, or event command
     */
    private static boolean isTaskCommand(String command) {
        return command.equals("todo") || command.startsWith("todo ")
                || command.equals("deadline") || command.startsWith("deadline ")
                || command.equals("event") || command.startsWith("event ");
    }

    /**
     * Adds a command to the task list and acknowledges the addition.
     *
     * @param taskList the in-memory task list
     * @param command the text entered by the user
     */
    private static void addTask(TaskList taskList, String command) throws AvonException {
        try {
            Task task = parseTask(command);
            taskList.add(task);
            printAddedTask(taskList, task);
        } catch (IllegalStateException exception) {
            System.out.println(AVON_PREFIX + exception.getMessage());
        }
    }

    /**
     * Displays all tasks in the order in which they were entered.
     *
     * @param taskList the in-memory task list
     */
    private static void printTasks(TaskList taskList) {
        if (taskList.size() == 0) {
            System.out.println(AVON_PREFIX + "Thy task list is empty.");
            return;
        }

        System.out.println(AVON_PREFIX + "Here are the tasks in thy list:");
        for (int index = 0; index < taskList.size(); index++) {
            String prefix = index == 0 ? AVON_PREFIX : "        ";
            System.out.println(prefix + (index + 1) + "." + taskList.getTask(index));
        }
    }

    /**
     * Marks the task identified by the one-based number in a mark command as done.
     *
     * @param taskList the in-memory task list
     * @param command the mark command entered by the user
     */
    private static void markTask(TaskList taskList, String command)
            throws InvalidTaskNumberException {
        int taskIndex = parseTaskIndex(taskList, command, "mark");
        Task task = taskList.getTask(taskIndex);
        task.markAsDone();
        System.out.println(AVON_PREFIX + "Tis well! Thy noble task is now fulfilled:");
        System.out.println("        " + task);
    }

    /**
     * Marks the task identified by the one-based number in an unmark command as not done.
     *
     * @param taskList the in-memory task list
     * @param command the unmark command entered by the user
     */
    private static void unmarkTask(TaskList taskList, String command)
            throws InvalidTaskNumberException {
        int taskIndex = parseTaskIndex(taskList, command, "unmark");
        Task task = taskList.getTask(taskIndex);
        task.markAsNotDone();
        System.out.println(AVON_PREFIX + "Thy noble task is undone once more:");
        System.out.println("        " + task);
    }

    /**
     * Parses and validates the one-based task number in a mark or unmark command.
     *
     * @param taskList the in-memory task list
     * @param command the complete command entered by the user
     * @param action the command's first word
     * @return the corresponding zero-based task index
     * @throws InvalidTaskNumberException if the number is missing, malformed, or out of range
     */
    private static int parseTaskIndex(TaskList taskList, String command, String action)
            throws InvalidTaskNumberException {
        String taskNumberText = command.substring(action.length()).trim();
        if (taskNumberText.isEmpty()) {
            throw new InvalidTaskNumberException(action,
                    "Add a task number after '" + action + "'.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(taskNumberText);
        } catch (NumberFormatException exception) {
            throw new InvalidTaskNumberException(action,
                    "Use one whole task number greater than zero.");
        }

        if (taskList.size() == 0) {
            throw new InvalidTaskNumberException(action,
                    "Your task list is empty; add a task first.");
        }
        if (taskNumber <= 0 || taskNumber > taskList.size()) {
            throw new InvalidTaskNumberException(action,
                    "Choose a task number from 1 to " + taskList.size() + ".");
        }
        return taskNumber - 1;
    }

    /**
     * Creates the appropriate task subtype for a command.
     *
     * @param command the command entered by the user
     * @return the task represented by the command
     */
    private static Task parseTask(String command) throws AvonException {
        if (command.equals("todo") || command.startsWith("todo ")) {
            return new Todo(extractDescription(command, "todo", "todo DESCRIPTION"));
        }
        if (command.equals("deadline") || command.startsWith("deadline ")) {
            return parseDeadline(command);
        }
        if (command.equals("event") || command.startsWith("event ")) {
            return parseEvent(command);
        }
        throw new UnknownCommandException();
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
        String example = "deadline DESCRIPTION /by DATE_OR_TIME";
        String details = extractDescription(command, "deadline",
                example);
        int byIndex = details.indexOf("/by");
        if (byIndex < 0) {
            throw new InvalidTaskFormatException("deadline",
                    "Include '/by' before the deadline date or time.", example);
        }
        if (details.indexOf("/by", byIndex + 3) >= 0) {
            throw new InvalidTaskFormatException("deadline",
                    "Use '/by' exactly once.", example);
        }
        if (byIndex == 0) {
            throw new EmptyDescriptionException("deadline", example);
        }

        String description = details.substring(0, byIndex).trim();
        String by = requireTaskDetail(details.substring(byIndex + 3).trim(),
                "deadline", "Add a date or time after '/by'.", example);
        return new Deadline(description, by);
    }

    /**
     * Parses an event command into an Event task.
     *
     * @param command the event command entered by the user
     * @return the parsed event task
     */
    private static Event parseEvent(String command) throws AvonException {
        String example = "event DESCRIPTION /from START /to END";
        String details = extractDescription(command, "event",
                example);
        int fromIndex = details.indexOf("/from");
        int toIndex = details.indexOf("/to");
        if (fromIndex < 0) {
            throw new InvalidTaskFormatException("event",
                    "Include '/from' before the start date or time.", example);
        }
        if (toIndex < 0) {
            throw new InvalidTaskFormatException("event",
                    "Include '/to' before the end date or time.", example);
        }
        if (details.indexOf("/from", fromIndex + 5) >= 0
                || details.indexOf("/to", toIndex + 3) >= 0) {
            throw new InvalidTaskFormatException("event",
                    "Use '/from' and '/to' exactly once each.", example);
        }
        if (toIndex < fromIndex) {
            throw new InvalidTaskFormatException("event",
                    "Place '/from' before '/to'.", example);
        }
        if (fromIndex == 0) {
            throw new EmptyDescriptionException("event", example);
        }

        String description = details.substring(0, fromIndex).trim();
        String from = requireTaskDetail(details.substring(fromIndex + 5, toIndex).trim(),
                "event", "Add a start date or time after '/from'.", example);
        String to = requireTaskDetail(details.substring(toIndex + 3).trim(),
                "event", "Add an end date or time after '/to'.", example);
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

    /**
     * Prints the acknowledgement for a newly added task.
     *
     * @param taskList the in-memory task list
     * @param task the task that was added
     */
    private static void printAddedTask(TaskList taskList, Task task) {
        System.out.println(AVON_PREFIX + "By thy command, I've added this task:");
        System.out.println("        " + task);
        System.out.println(AVON_PREFIX + "Now thou hast " + taskList.size() + " tasks in thy list.");
    }

}
