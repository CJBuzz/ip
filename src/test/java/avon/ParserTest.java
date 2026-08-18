package avon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ParserTest {
    @Test
    void parseTask_validDeadline_returnsDeadline() throws AvonException {
        Task task = Parser.parseTask("deadline return book /by 2026-08-20", CommandType.DEADLINE);

        assertInstanceOf(Deadline.class, task);
        assertEquals("[D][ ] return book (by: Aug 20 2026)", task.toString());
    }

    @Test
    void parseTask_invalidDeadline_throwsInvalidTaskFormatException() {
        assertThrows(InvalidTaskFormatException.class,
                () -> Parser.parseTask("deadline return book /by tomorrow", CommandType.DEADLINE));
    }

    @Test
    void parseTask_validEvent_returnsEvent() throws AvonException {
        Task task = Parser.parseTask("event lecture /from 2pm /to 4pm", CommandType.EVENT);

        assertInstanceOf(Event.class, task);
        assertEquals("[E][ ] lecture (from: 2pm to: 4pm)", task.toString());
    }
}
