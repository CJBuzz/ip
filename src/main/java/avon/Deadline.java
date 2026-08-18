package avon;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that must be completed by a specified date.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy");

    private final LocalDate by;

    /**
     * Creates an unfinished deadline task.
     *
     * @param description the text describing the task
     * @param by the date by which the task should be completed
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the deadline in Avon's tab-separated storage format.
     *
     * @return the serialized deadline
     */
    @Override
    public String toDataString() {
        return "D\t" + isDone + "\t" + description + "\t" + by;
    }

    /**
     * Returns this deadline with its type, completion status, and due date.
     *
     * @return the formatted deadline task
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }
}
