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

            if (command.equals("bye")) {
                System.out.println(AVON_PREFIX + "Fare thee well! Pray heavens our paths cross anon.");
                System.out.println(SEPARATOR);
                break;
            }

            if (command.equals("list")) {
                printTasks(taskList);
            } else if (command.equals("mark") || command.startsWith("mark ")) {
                markTask(taskList, command);
            } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                unmarkTask(taskList, command);
            } else {
                addTask(taskList, command);
            }
            System.out.println(SEPARATOR);
        }
        scanner.close();
    }

    /**
     * Adds a command to the task list and acknowledges the addition.
     *
     * @param taskList the in-memory task list
     * @param command the text entered by the user
     */
    private static void addTask(TaskList taskList, String command) {
        try {
            Task task = parseTask(command);
            taskList.add(task);
            printAddedTask(taskList, task);
        } catch (IllegalStateException exception) {
            System.out.println(AVON_PREFIX + exception.getMessage());
        } catch (IllegalArgumentException exception) {
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
            System.out.println(AVON_PREFIX + "Thy quest list is empty.");
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
    private static void markTask(TaskList taskList, String command) {
        try {
            int taskNumber = Integer.parseInt(command.substring("mark".length()).trim());
            int taskIndex = taskNumber - 1;
            Task task = taskList.getTask(taskIndex);
            task.markAsDone();
            System.out.println(AVON_PREFIX + "Tis well! Thy noble quest is now fulfilled:");
            System.out.println("        " + task);
        } catch (NumberFormatException exception) {
            System.out.println(AVON_PREFIX + "I pray thee, speak a rightful number after 'mark'.");
        } catch (IndexOutOfBoundsException exception) {
            System.out.println(AVON_PREFIX + "Alas, no such deed of that number can be found!");
        }
    }

    /**
     * Marks the task identified by the one-based number in an unmark command as not done.
     *
     * @param taskList the in-memory task list
     * @param command the unmark command entered by the user
     */
    private static void unmarkTask(TaskList taskList, String command) {
        try {
            int taskNumber = Integer.parseInt(command.substring("unmark".length()).trim());
            int taskIndex = taskNumber - 1;
            Task task = taskList.getTask(taskIndex);
            task.markAsNotDone();
            System.out.println(AVON_PREFIX + "Thy noble quest is undone once more:");
            System.out.println("        " + task);
        } catch (NumberFormatException exception) {
            System.out.println(AVON_PREFIX + "I pray thee, speak a rightful number after 'unmark'.");
        } catch (IndexOutOfBoundsException exception) {
            System.out.println(AVON_PREFIX + "Alas, no such deed of that number can be found!");
        }
    }

    /**
     * Creates the appropriate task subtype for a command.
     *
     * @param command the command entered by the user
     * @return the task represented by the command
     */
    private static Task parseTask(String command) {
        if (command.equals("todo") || command.startsWith("todo ")) {
            return new Todo(extractDescription(command, "todo"));
        }
        if (command.equals("deadline") || command.startsWith("deadline ")) {
            return parseDeadline(command);
        }
        if (command.equals("event") || command.startsWith("event ")) {
            return parseEvent(command);
        }
        return new Todo(requireValue(command.trim(), "A task description is required."));
    }

    /**
     * Extracts a command's description after its keyword.
     *
     * @param command the command entered by the user
     * @param keyword the command keyword to remove
     * @return the non-empty task description
     */
    private static String extractDescription(String command, String keyword) {
        return requireValue(command.substring(keyword.length()).trim(),
                "A task description is required.");
    }

    /**
     * Parses a deadline command into a Deadline task.
     *
     * @param command the deadline command entered by the user
     * @return the parsed deadline task
     */
    private static Deadline parseDeadline(String command) {
        String details = extractDescription(command, "deadline");
        int byIndex = details.indexOf("/by");
        if (byIndex <= 0) {
            throw new IllegalArgumentException("A deadline must include '/by'.");
        }

        String description = requireValue(details.substring(0, byIndex).trim(),
                "A task description is required before '/by'.");
        String by = requireValue(details.substring(byIndex + 3).trim(),
                "A deadline date or time is required after '/by'.");
        return new Deadline(description, by);
    }

    /**
     * Parses an event command into an Event task.
     *
     * @param command the event command entered by the user
     * @return the parsed event task
     */
    private static Event parseEvent(String command) {
        String details = extractDescription(command, "event");
        int fromIndex = details.indexOf("/from");
        int toIndex = details.indexOf("/to", fromIndex + 5);
        if (fromIndex <= 0 || toIndex <= fromIndex) {
            throw new IllegalArgumentException("An event must include '/from' and '/to'.");
        }

        String description = requireValue(details.substring(0, fromIndex).trim(),
                "A task description is required before '/from'.");
        String from = requireValue(details.substring(fromIndex + 5, toIndex).trim(),
                "An event start time is required after '/from'.");
        String to = requireValue(details.substring(toIndex + 3).trim(),
                "An event end time is required after '/to'.");
        return new Event(description, from, to);
    }

    /**
     * Ensures that a parsed command component is not empty.
     *
     * @param value the component to validate
     * @param message the error message for an empty component
     * @return the original non-empty component
     */
    private static String requireValue(String value, String message) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException(message);
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
