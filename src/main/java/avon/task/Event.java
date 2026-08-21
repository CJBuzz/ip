package avon.task;

import java.time.LocalDateTime;

import avon.util.DateTimeParser;

/**
 * Represents a task that takes place during a specified time interval.
 */
public class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Creates an unfinished event task.
     *
     * @param description the text describing the event.
     * @param from the event start date and time.
     * @param to the event end date and time.
     * @throws IllegalArgumentException if the event ends before it starts.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("An event cannot end before it starts.");
        }
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event start date and time.
     *
     * @return the event start.
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Returns the event end date and time.
     *
     * @return the event end.
     */
    public LocalDateTime getTo() {
        return to;
    }

    /**
     * Returns this event with its type, completion status, and time interval.
     *
     * @return the formatted event task.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + DateTimeParser.format(from)
                + " to: " + DateTimeParser.format(to) + ")";
    }
}
