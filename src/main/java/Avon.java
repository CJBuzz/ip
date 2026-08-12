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
            taskList.add(command);
            System.out.println(AVON_PREFIX + "added: " + command);
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
            System.out.println(AVON_PREFIX + "Thy quest list is empty.");
            return;
        }

        for (int index = 0; index < taskList.size(); index++) {
            String prefix = index == 0 ? AVON_PREFIX : "        ";
            String status = taskList.isDone(index) ? "[X] " : "[ ] ";
            System.out.println(prefix + (index + 1) + ". " + status + taskList.get(index));
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
            taskList.markDone(taskIndex);
            System.out.println(AVON_PREFIX + "Tis well! Thy noble quest is now fulfilled:");
            System.out.println("        [X] " + taskList.get(taskIndex));
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
            taskList.markUndone(taskIndex);
            System.out.println(AVON_PREFIX + "Thy noble quest is undone once more:");
            System.out.println("        [ ] " + taskList.get(taskIndex));
        } catch (NumberFormatException exception) {
            System.out.println(AVON_PREFIX + "I pray thee, speak a rightful number after 'unmark'.");
        } catch (IndexOutOfBoundsException exception) {
            System.out.println(AVON_PREFIX + "Alas, no such deed of that number can be found!");
        }
    }
}
