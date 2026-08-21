package avon.task;

/**
 * Represents a task and whether it has been completed.
 */
public abstract class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates an unfinished task with the given description.
     *
     * @param description the text describing the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon used when displaying this task.
     *
     * @return {@code X} for a completed task, or a space otherwise.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the task description for persistence and searching.
     *
     * @return the text describing this task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether this task has been completed.
     *
     * @return {@code true} if the task is completed.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Converts this task into Avon's tab-separated storage format.
     *
     * @return the serialized task.
     */
    public abstract String toDataString();

    /**
     * Returns this task in the format used by Avon’s task list.
     *
     * @return the status icon and description of this task.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + getDescription();
    }
}
