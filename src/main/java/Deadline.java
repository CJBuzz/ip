/**
 * Represents a task that must be completed by a specified date or time.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates an unfinished deadline task.
     *
     * @param description the text describing the task
     * @param by the date or time by which the task should be completed
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the deadline description.
     *
     * @return the date or time by which the task should be completed
     */
    public String getBy() {
        return by;
    }

    /**
     * Returns this deadline with its type, completion status, and due date.
     *
     * @return the formatted deadline task
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
