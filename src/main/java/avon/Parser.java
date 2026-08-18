package avon;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Interprets user commands and converts their arguments into domain objects.
 */
public class Parser {
    /**
     * Identifies the type of a complete command.
     *
     * @param command the command entered by the user
     * @return the matching command type
     * @throws UnknownCommandException if the command is unsupported
     */
    public static CommandType parseCommandType(String command) throws UnknownCommandException {
        return CommandType.parse(command);
    }

    /**
     * Creates the appropriate task subtype for a command.
     *
     * @param command the command entered by the user
     * @param commandType the type of task to create
     * @return the task represented by the command
     * @throws AvonException if the task details are invalid
     */
    public static Task parseTask(String command, CommandType commandType) throws AvonException {
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
     * Parses and validates the one-based task number in a task-number command.
     *
     * @param taskList the in-memory task list
     * @param command the complete command entered by the user
     * @param commandType the type of task-number command
     * @return the corresponding zero-based task index
     * @throws InvalidTaskNumberException if the number is missing, malformed, or out of range
     */
    public static int parseTaskIndex(TaskList taskList, String command, CommandType commandType)
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
     * Extracts the required keyword from a find command.
     *
     * @param command the complete find command
     * @return the non-empty keyword to search for
     * @throws EmptyDescriptionException if the command has no keyword
     */
    public static String parseFindKeyword(String command) throws EmptyDescriptionException {
        String findKeyword = CommandType.FIND.getKeyword();
        return extractDescription(command, findKeyword, "find KEYWORD");
    }

    /**
     * Parses a deadline command into a deadline task.
     *
     * @param command the deadline command entered by the user
     * @return the parsed deadline task
     * @throws AvonException if required details are missing or the date is invalid
     */
    private static Deadline parseDeadline(String command) throws AvonException {
        String deadlineKeyword = CommandType.DEADLINE.getKeyword();
        String example = "deadline DESCRIPTION /by yyyy-MM-dd [HHmm]";
        String details = extractDescription(command, deadlineKeyword, example);
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
            return new Deadline(description, DateTimeParser.parse(by));
        } catch (DateTimeParseException exception) {
            throw new InvalidTaskFormatException(deadlineKeyword,
                    "Use a real date and optional 24-hour time in yyyy-MM-dd [HHmm] format.",
                    example);
        }
    }

    /**
     * Parses an event command into an event task.
     *
     * @param command the event command entered by the user
     * @return the parsed event task
     * @throws AvonException if required details are missing or misplaced
     */
    private static Event parseEvent(String command) throws AvonException {
        String eventKeyword = CommandType.EVENT.getKeyword();
        String example = "event DESCRIPTION /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm";
        String details = extractDescription(command, eventKeyword, example);
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
        String fromText = requireTaskDetail(details.substring(fromIndex + 5, toIndex).trim(),
                eventKeyword, "Add a start date or time after '/from'.", example);
        String toText = requireTaskDetail(details.substring(toIndex + 3).trim(),
                eventKeyword, "Add an end date or time after '/to'.", example);
        try {
            return createEvent(description, DateTimeParser.parse(fromText),
                    DateTimeParser.parse(toText), eventKeyword, example);
        } catch (DateTimeParseException exception) {
            throw new InvalidTaskFormatException(eventKeyword,
                    "Use real dates and optional 24-hour times in yyyy-MM-dd [HHmm] format.",
                    example);
        }
    }

    /**
     * Creates an event after ensuring its interval runs forwards in time.
     *
     * @param description the event description
     * @param from the event start
     * @param to the event end
     * @param eventKeyword the event command keyword
     * @param example a complete example of the expected command
     * @return the validated event
     * @throws InvalidTaskFormatException if the event ends before it starts
     */
    private static Event createEvent(String description, LocalDateTime from,
            LocalDateTime to, String eventKeyword, String example)
            throws InvalidTaskFormatException {
        if (to.isBefore(from)) {
            throw new InvalidTaskFormatException(eventKeyword,
                    "Set '/to' to the same time as or later than '/from'.", example);
        }
        return new Event(description, from, to);
    }

    /**
     * Extracts a non-empty task description after a command keyword.
     *
     * @param command the complete command
     * @param keyword the command keyword to remove
     * @param example a complete example of the expected command
     * @return the task description
     * @throws EmptyDescriptionException if no description follows the keyword
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
