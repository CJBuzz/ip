package avon.task;

import java.time.LocalDateTime;

import avon.util.DateTimeParser;
import avon.util.StorageFieldCodec;

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
     * Returns the event in Avon's tab-separated storage format.
     *
     * @return the serialized event.
     */
    @Override
    public String toDataString() {
        return "E2\t" + isDone + "\t" + StorageFieldCodec.escape(description)
                + "\t" + from + "\t" + to;
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
