/**
 * Stores tasks in memory for the duration of an Avon session.
 */
public class TaskList {
    private static final int MAX_TASKS = 100;
    private final Task[] tasks = new Task[MAX_TASKS];
    private int taskCount;

    /**
     * Adds a todo task with the given description to the end of the list.
     *
     * @param description the text of the task to store
     * @throws IllegalStateException if the list already contains 100 tasks
     */
    public void add(String description) {
        add(new Todo(description));
    }

    /**
     * Adds a task of any supported type to the end of the list.
     *
     * @param task the task to store
     * @throws IllegalArgumentException if the task is null
     * @throws IllegalStateException if the list already contains 100 tasks
     */
    public void add(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("A task cannot be null.");
        }

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
     * Removes and returns the task at the requested zero-based index.
     *
     * @param index the zero-based position of the task to remove
     * @return the task that was removed
     * @throws IndexOutOfBoundsException if the index does not refer to a stored task
     */
    public Task removeTask(int index) {
        validateIndex(index);
        Task removedTask = tasks[index];
        shiftTasksLeft(index);
        taskCount--;
        tasks[taskCount] = null;
        return removedTask;
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
     * Shifts every task after the removed index one position to the left.
     *
     * @param removedIndex the zero-based position of the removed task
     */
    private void shiftTasksLeft(int removedIndex) {
        for (int index = removedIndex; index < taskCount - 1; index++) {
            tasks[index] = tasks[index + 1];
        }
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
