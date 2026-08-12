/**
 * Stores tasks in memory for the duration of an Avon session.
 */
public class TaskList {
    private static final int MAX_TASKS = 100;
    private final Task[] tasks = new Task[MAX_TASKS];
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

        tasks[taskCount] = new Task(task);
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
     * Returns the description of the task at the requested zero-based index.
     *
     * @param index the zero-based position of the task
     * @return the task description at the requested position
     * @throws IndexOutOfBoundsException if the index does not refer to a stored task
     */
    public String get(int index) {
        validateIndex(index);
        return tasks[index].getDescription();
    }

    /**
     * Returns the task at the requested zero-based index.
     *
     * @param index the zero-based position of the task
     * @return the task at the requested position
     * @throws IndexOutOfBoundsException if the index does not refer to a stored task
     */
    public Task getTask(int index) {
        validateIndex(index);
        return tasks[index];
    }

    /**
     * Marks the task at the requested zero-based index as done.
     *
     * @param index the zero-based position of the task
     * @throws IndexOutOfBoundsException if the index does not refer to a stored task
     */
    public void markDone(int index) {
        getTask(index).markAsDone();
    }

    /**
     * Marks the task at the requested zero-based index as not done.
     *
     * @param index the zero-based position of the task
     * @throws IndexOutOfBoundsException if the index does not refer to a stored task
     */
    public void markUndone(int index) {
        getTask(index).markAsNotDone();
    }

    /**
     * Checks whether the task at the requested zero-based index is done.
     *
     * @param index the zero-based position of the task
     * @return true if the task is done
     * @throws IndexOutOfBoundsException if the index does not refer to a stored task
     */
    public boolean isDone(int index) {
        return getTask(index).isDone();
    }

    /**
     * Ensures that an index points to a task currently stored in the list.
     *
     * @param index the zero-based position to validate
     * @throws IndexOutOfBoundsException if the index does not refer to a stored task
     */
    private void validateIndex(int index) {
        if (index < 0 || index >= taskCount) {
            throw new IndexOutOfBoundsException("Task index is outside the task list.");
        }
    }
}
