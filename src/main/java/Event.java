/**
 * Represents a task that takes place during a specified time interval.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an unfinished event task.
     *
     * @param description the text describing the event
     * @param from the event start date and/or time
     * @param to the event end date and/or time
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this event with its type, completion status, and time interval.
     *
     * @return the formatted event task
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
