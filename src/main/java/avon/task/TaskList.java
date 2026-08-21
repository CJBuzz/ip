package avon.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores tasks in memory for the duration of an Avon session.
 */
public class TaskList {
    private final List<Task> tasks = new ArrayList<>();

    /**
     * Creates an empty task list.
     */
    public TaskList() {
    }

    /**
     * Creates a task list containing tasks loaded from storage.
     *
     * @param tasks the tasks with which to initialize the list.
     */
    public TaskList(List<Task> tasks) {
        this.tasks.addAll(tasks);
    }

    /**
     * Adds a task of any supported type to the end of the list.
     *
     * @param task the task to store.
     * @throws IllegalArgumentException if the task is null.
     */
    public void add(Task task) {
        if (task == null) {
            // Guard against invalid tasks introduced by future changes.
            throw new IllegalArgumentException("A task cannot be null.");
        }
        tasks.add(task);
    }

    /**
     * Inserts a task at the requested zero-based index.
     *
     * @param index the zero-based position at which to insert the task.
     * @param task the task to store.
     * @throws IllegalArgumentException if the task is null.
     * @throws IndexOutOfBoundsException if the index is outside the insertion range.
     */
    public void add(int index, Task task) {
        if (task == null) {
            throw new IllegalArgumentException("A task cannot be null.");
        }
        tasks.add(index, task);
    }

    /**
     * Returns the number of tasks currently stored.
     *
     * @return the number of stored tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at the requested zero-based index.
     *
     * @param index the zero-based position of the task.
     * @return the task at the requested position.
     * @throws IndexOutOfBoundsException if the index does not refer to a stored task.
     */
    public Task getTask(int index) {
        validateIndex(index);
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at the requested zero-based index.
     *
     * @param index the zero-based position of the task to remove.
     * @return the task that was removed.
     * @throws IndexOutOfBoundsException if the index does not refer to a stored task.
     */
    public Task removeTask(int index) {
        validateIndex(index);
        return tasks.remove(index);
    }

    /**
     * Returns tasks whose descriptions contain the given keyword.
     *
     * @param keyword the text to find in task descriptions.
     * @return matching tasks in their original order.
     */
    public List<Task> find(String keyword) {
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /**
     * Ensures that an index points to a task currently stored in the list.
     *
     * @param index the zero-based position to validate.
     * @throws IndexOutOfBoundsException if the index does not refer to a stored task.
     */
    private void validateIndex(int index) {
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundsException("Task index is outside the task list.");
        }
    }
}
