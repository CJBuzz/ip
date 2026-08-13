import java.util.ArrayList;
import java.util.List;

/**
 * Stores tasks in memory for the duration of an Avon session.
 */
public class TaskList {
    private final List<Task> tasks = new ArrayList<>();

    /**
     * Adds a todo task with the given description to the end of the list.
     *
     * @param description the text of the task to store
     */
    public void add(String description) {
        add(new Todo(description));
    }

    /**
     * Adds a task of any supported type to the end of the list.
     *
     * @param task the task to store
     * @throws IllegalArgumentException if the task is null
     */
    public void add(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("A task cannot be null.");
        }
        tasks.add(task);
    }

    /**
     * Returns the number of tasks currently stored.
     *
     * @return the number of stored tasks
     */
    public int size() {
        return tasks.size();
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
        return tasks.get(index).getDescription();
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
        return tasks.get(index);
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
        return tasks.remove(index);
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
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundsException("Task index is outside the task list.");
        }
    }
}
