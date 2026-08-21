package avon.ui;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import avon.exception.AvonException;
import avon.task.Task;
import avon.task.TaskList;

/**
 * Handles all command-line input and output for Avon.
 */
public class Ui {
    private static final String SEPARATOR = "____________________________________________________________";
    private static final String AVON_PREFIX = "Avon:\t";
    private static final String INDENT = "        ";

    private final Scanner scanner;
    private final PrintStream output;

    /**
     * Creates a UI that reads from standard input and writes to standard output.
     */
    public Ui() {
        this(System.in, System.out);
    }

    /**
     * Creates a UI backed by the specified input and output streams.
     *
     * @param input the stream from which commands are read.
     * @param output the stream to which responses are written.
     */
    public Ui(InputStream input, PrintStream output) {
        scanner = new Scanner(input);
        this.output = output;
    }

    /**
     * Displays Avon's banner and greeting.
     */
    public void showWelcome() {
        String banner = """
                ___
               /   |_   ______  ____
              / /| | | / / __ \\/ __ \\
             / ___ | |/ / /_/ / / / /
            /_/  |_|___/\\____/_/ /_/ """;
        output.println(SEPARATOR);
        output.println(banner);
        output.println(AVON_PREFIX + "Hark! I am Avon who stands before thee.");
        output.println(AVON_PREFIX + "How may my hand or wit now serve thy need?");
        output.println(SEPARATOR);
    }

    /**
     * Returns whether another command is available from standard input.
     *
     * @return {@code true} if another command can be read.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next complete command.
     *
     * @return the command entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the divider between command responses.
     */
    public void showSeparator() {
        output.println(SEPARATOR);
    }

    /**
     * Displays Avon's farewell.
     */
    public void showGoodbye() {
        output.println(AVON_PREFIX + "Fare thee well! Pray heavens our paths cross anon.");
    }

    /**
     * Displays all tasks in their current order.
     *
     * @param taskList the tasks to display.
     */
    public void showTaskList(TaskList taskList) {
        if (taskList.size() == 0) {
            output.println(AVON_PREFIX + "Thy task list is empty.");
            return;
        }

        output.println(AVON_PREFIX + "Here are the tasks in thy list:");
        for (int index = 0; index < taskList.size(); index++) {
            output.println(INDENT + (index + 1) + "." + taskList.getTask(index));
        }
    }

    /**
     * Displays tasks that match a search keyword.
     *
     * @param matchingTasks the matching tasks in their original order.
     */
    public void showMatchingTasks(List<Task> matchingTasks) {
        output.println(AVON_PREFIX + "Here are the matching tasks in thy list:");
        for (int index = 0; index < matchingTasks.size(); index++) {
            output.println(INDENT + (index + 1) + "." + matchingTasks.get(index));
        }
    }

    /**
     * Displays confirmation that a task was added.
     *
     * @param task the task that was added.
     * @param taskCount the resulting number of tasks.
     */
    public void showAddedTask(Task task, int taskCount) {
        output.println(AVON_PREFIX + "By thy command, I've added this task:");
        output.println(INDENT + task);
        showTaskCount(taskCount);
    }

    /**
     * Displays confirmation that a task was marked as completed.
     *
     * @param task the completed task.
     */
    public void showMarkedTask(Task task) {
        output.println(AVON_PREFIX + "Tis well! Thy noble task is now fulfilled:");
        output.println(INDENT + task);
    }

    /**
     * Displays confirmation that a task was marked as not completed.
     *
     * @param task the uncompleted task.
     */
    public void showUnmarkedTask(Task task) {
        output.println(AVON_PREFIX + "Thy noble task is undone once more:");
        output.println(INDENT + task);
    }

    /**
     * Displays confirmation that a task was removed.
     *
     * @param task the removed task.
     * @param taskCount the resulting number of tasks.
     */
    public void showDeletedTask(Task task, int taskCount) {
        output.println(AVON_PREFIX + "So be it! I've removed this task:");
        output.println(INDENT + task);
        showTaskCount(taskCount);
    }

    /**
     * Displays a user-facing exception message.
     *
     * @param exception the exception to display.
     */
    public void showError(AvonException exception) {
        String indentedMessage = exception.getMessage().replace("\n", "\n" + INDENT);
        output.println(AVON_PREFIX + indentedMessage);
    }

    /**
     * Closes the input scanner.
     */
    public void close() {
        scanner.close();
    }

    /**
     * Displays the current number of tasks.
     *
     * @param taskCount the number of tasks.
     */
    private void showTaskCount(int taskCount) {
        output.println(AVON_PREFIX + "Now thou hast " + taskCount + " tasks in thy list.");
    }
}
