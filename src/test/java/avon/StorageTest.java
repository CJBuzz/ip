package avon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class StorageTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void saveAndLoad_multipleTaskTypes_restoresTasks() throws StorageException {
        Path dataFile = temporaryDirectory.resolve("data/avon.txt");
        Storage storage = new Storage(dataFile);
        Todo todo = new Todo("read Hamlet");
        todo.markAsDone();
        TaskList taskList = new TaskList(List.of(
                todo,
                new Deadline("return book", LocalDateTime.of(2026, 8, 20, 18, 0)),
                new Event("lecture", "2pm", "4pm")));

        storage.save(taskList);
        List<Task> loadedTasks = storage.load();

        assertEquals(3, loadedTasks.size());
        assertTrue(loadedTasks.get(0).isDone());
        assertEquals("[D][ ] return book (by: Aug 20 2026, 6:00PM)",
                loadedTasks.get(1).toString());
        assertEquals("[E][ ] lecture (from: 2pm to: 4pm)", loadedTasks.get(2).toString());
    }

    @Test
    void load_malformedTaskData_throwsStorageException() throws IOException {
        Path dataFile = temporaryDirectory.resolve("avon.txt");
        Files.writeString(dataFile, "D\tfalse\tmissing date");

        Storage storage = new Storage(dataFile);

        assertThrows(StorageException.class, storage::load);
    }
}
