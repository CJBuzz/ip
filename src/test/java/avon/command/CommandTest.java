package avon.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import avon.exception.DuplicateTaskException;
import avon.exception.StorageException;
import avon.storage.Storage;
import avon.task.TaskList;
import avon.task.Todo;
import avon.ui.Ui;

class CommandTest {
    private ByteArrayOutputStream output;
    private Ui ui;
    private Storage failingStorage;

    @BeforeEach
    void setUp() {
        output = new ByteArrayOutputStream();
        ui = new Ui(new ByteArrayInputStream(new byte[0]), new PrintStream(output));
        failingStorage = new FailingStorage();
    }

    @Test
    void addCommand_saveFails_restoresTaskListAndShowsNoSuccess() {
        TaskList taskList = new TaskList();

        assertThrows(StorageException.class, () ->
                new AddCommand(new Todo("read Hamlet")).execute(taskList, ui, failingStorage));

        assertEquals(0, taskList.size());
        assertEquals("", output.toString());
    }

    @Test
    void addCommand_duplicateTask_throwsDuplicateTaskExceptionAndDoesNotAdd() {
        TaskList taskList = new TaskList(List.of(new Todo("read Hamlet")));

        assertThrows(DuplicateTaskException.class, () ->
                new AddCommand(new Todo("read Hamlet")).execute(taskList, ui, failingStorage));

        assertEquals(1, taskList.size());
        assertEquals("", output.toString());
    }

    @Test
    void deleteCommand_saveFails_restoresTaskAtOriginalPositionAndShowsNoSuccess() {
        TaskList taskList = new TaskList(List.of(new Todo("first"), new Todo("second")));

        assertThrows(StorageException.class, () ->
                new DeleteCommand(1).execute(taskList, ui, failingStorage));

        assertEquals(2, taskList.size());
        assertEquals("first", taskList.getTask(0).getDescription());
        assertEquals("", output.toString());
    }

    @Test
    void markCommand_saveFails_restoresCompletionStateAndShowsNoSuccess() {
        TaskList taskList = new TaskList(List.of(new Todo("read Hamlet")));

        assertThrows(StorageException.class, () ->
                new MarkCommand(1).execute(taskList, ui, failingStorage));

        assertFalse(taskList.getTask(0).isDone());
        assertEquals("", output.toString());
    }

    @Test
    void unmarkCommand_saveFails_restoresCompletionStateAndShowsNoSuccess() {
        Todo todo = new Todo("read Hamlet");
        todo.markAsDone();
        TaskList taskList = new TaskList(List.of(todo));

        assertThrows(StorageException.class, () ->
                new UnmarkCommand(1).execute(taskList, ui, failingStorage));

        assertTrue(taskList.getTask(0).isDone());
        assertEquals("", output.toString());
    }

    private static class FailingStorage extends Storage {
        private FailingStorage() {
            super(Path.of("unused"));
        }

        /** {@inheritDoc} */
        @Override
        public void save(TaskList taskList) throws StorageException {
            throw new StorageException("Simulated save failure.");
        }
    }
}
