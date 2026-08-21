package avon.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import avon.command.AddCommand;
import avon.command.Command;
import avon.command.CommandType;
import avon.command.DeleteCommand;
import avon.command.ExitCommand;
import avon.command.FindCommand;
import avon.command.ListCommand;
import avon.command.MarkCommand;
import avon.command.UnmarkCommand;
import avon.exception.AvonException;
import avon.exception.InvalidTaskFormatException;
import avon.task.Deadline;
import avon.task.Event;
import avon.task.Task;

class ParserTest {
    @Test
    void parse_validTaskCommand_returnsAddCommand() throws AvonException {
        Command command = Parser.parse("todo read Hamlet");

        assertInstanceOf(AddCommand.class, command);
    }

    @Test
    void parse_supportedNonTaskCommands_returnSpecializedCommands() throws AvonException {
        assertInstanceOf(ListCommand.class, Parser.parse("list"));
        assertInstanceOf(FindCommand.class, Parser.parse("find Hamlet"));
        assertInstanceOf(MarkCommand.class, Parser.parse("mark 1"));
        assertInstanceOf(UnmarkCommand.class, Parser.parse("unmark 1"));
        assertInstanceOf(DeleteCommand.class, Parser.parse("delete 1"));
    }

    @Test
    void parse_byeCommand_returnsExitCommand() throws AvonException {
        Command command = Parser.parse("bye");

        assertInstanceOf(ExitCommand.class, command);
        assertTrue(command.isExit());
    }

    @Test
    void parse_commandsWithSurroundingOrSeparatorWhitespace_returnsCommands()
            throws AvonException {
        assertInstanceOf(AddCommand.class, Parser.parse("  todo read Hamlet  "));
        assertInstanceOf(MarkCommand.class, Parser.parse("mark\t1"));
        assertInstanceOf(ListCommand.class, Parser.parse("list   "));
    }

    @Test
    void parseTask_validDeadline_returnsDeadline() throws AvonException {
        Task task = Parser.parseTask("deadline return book /by 2026-08-20 1800", CommandType.DEADLINE);

        assertInstanceOf(Deadline.class, task);
        assertEquals("[D][ ] return book (by: Aug 20 2026, 6:00PM)", task.toString());
    }

    @Test
    void parseTask_dateOnlyDeadline_returnsDeadlineWithoutTime() throws AvonException {
        Task task = Parser.parseTask("deadline return book /by 2026-08-20", CommandType.DEADLINE);

        assertEquals("[D][ ] return book (by: Aug 20 2026)", task.toString());
    }

    @Test
    void parseTask_invalidDeadline_throwsInvalidTaskFormatException() {
        assertThrows(InvalidTaskFormatException.class,
                () -> Parser.parseTask("deadline return book /by tomorrow", CommandType.DEADLINE));
    }

    @Test
    void parseTask_validEvent_returnsEvent() throws AvonException {
        Task task = Parser.parseTask(
                "event lecture /from 2026-08-20 1400 /to 2026-08-20 1600",
                CommandType.EVENT);

        assertInstanceOf(Event.class, task);
        assertEquals("[E][ ] lecture (from: Aug 20 2026, 2:00PM to: Aug 20 2026, 4:00PM)",
                task.toString());
    }

    @Test
    void parseTask_eventEndingBeforeStart_throwsInvalidTaskFormatException() {
        assertThrows(InvalidTaskFormatException.class,
                () -> Parser.parseTask(
                        "event lecture /from 2026-08-20 1600 /to 2026-08-20 1400",
                        CommandType.EVENT));
    }

    @Test
    void parseTask_invalidEventDate_throwsInvalidTaskFormatException() {
        assertThrows(InvalidTaskFormatException.class,
                () -> Parser.parseTask(
                        "event lecture /from 2026-02-30 1400 /to 2026-02-30 1600",
                        CommandType.EVENT));
    }

    @Test
    void parseTask_delimiterPrefixesInDescriptions_preservesDescriptions()
            throws AvonException {
        Task deadline = Parser.parseTask(
                "deadline study /byte encoding /by 2026-08-20", CommandType.DEADLINE);
        Task event = Parser.parseTask(
                "event prepare /today notes /from 2026-08-20 1400 /to 2026-08-20 1600",
                CommandType.EVENT);

        assertEquals("study /byte encoding", deadline.getDescription());
        assertEquals("prepare /today notes", event.getDescription());
    }
}
