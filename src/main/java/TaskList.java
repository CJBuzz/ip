/**
 * Stores tasks in memory for the duration of an Avon session.
 */
public class TaskList {
    private static final int MAX_TASKS = 100;
    private final String[] tasks = new String[MAX_TASKS];
    private int taskCount;

    /**
     * Adds a task to the end of the list.
     *
     * @param task the text of the task to store
     * @throws IllegalStateException if the list already contains 100 tasks
     */
    public void add(String task) {
        if (taskCount == MAX_TASKS) {
            throw new IllegalStateException("The task list can hold no more than 100 tasks.");
        }

        tasks[taskCount] = task;
        taskCount++;
    }

    /**
     * Returns the number of tasks currently stored.
     *
     * @return the number of stored tasks
     */
    public int size() {
        return taskCount;
    }

    /**
     * Returns the task at the requested zero-based index.
     *
     * @param index the zero-based position of the task
     * @return the task at the requested position
     * @throws IndexOutOfBoundsException if the index does not refer to a stored task
     */
    public String get(int index) {
        if (index < 0 || index >= taskCount) {
            throw new IndexOutOfBoundsException("Task index is outside the task list.");
        }

        return tasks[index];
    }
}
