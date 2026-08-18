package avon.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;

import avon.exception.StorageException;
import avon.task.Deadline;
import avon.task.Event;
import avon.task.Task;
import avon.task.TaskList;
import avon.task.Todo;
import avon.util.DateTimeParser;

/**
 * Loads and saves Avon's tasks in a human-readable text file.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates storage backed by the specified file.
     *
     * @param filePath the path of the task data file.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads all saved tasks, or returns an empty list when no data file exists.
     *
     * @return the saved tasks.
     * @throws StorageException if the data cannot be read or parsed.
     */
    public List<Task> load() throws StorageException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        try {
            List<Task> tasks = new ArrayList<>();
            for (String line : Files.readAllLines(filePath)) {
                if (!line.isBlank()) {
                    tasks.add(parseTask(line));
                }
            }
            return tasks;
        } catch (IOException | DateTimeException | IllegalArgumentException exception) {
            throw new StorageException("I could not read thy saved tasks.");
        }
    }

    /**
     * Saves all current tasks, creating the parent directory when necessary.
     *
     * @param taskList the tasks to save.
     * @throws StorageException if the data cannot be written.
     */
    public void save(TaskList taskList) throws StorageException {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            List<String> lines = new ArrayList<>();
            for (int index = 0; index < taskList.size(); index++) {
                lines.add(taskList.getTask(index).toDataString());
            }
            Files.write(filePath, lines);
        } catch (IOException exception) {
            throw new StorageException("I could not preserve thy tasks upon the disk.");
        }
    }

    /**
     * Recreates one task from a line in the storage file.
     *
     * @param line the serialized task.
     * @return the restored task.
     */
    private Task parseTask(String line) {
        String[] fields = line.split("\\t", -1);
        if (fields.length < 3) {
            throw new IllegalArgumentException("Incomplete task data.");
        }

        Task task;
        switch (fields[0]) {
            case "T":
                requireFieldCount(fields, 3);
                task = new Todo(fields[2]);
                break;
            case "D":
                requireFieldCount(fields, 4);
                task = new Deadline(fields[2], DateTimeParser.parseStoredValue(fields[3]));
                break;
            case "E":
                requireFieldCount(fields, 5);
                task = new Event(fields[2], DateTimeParser.parseStoredValue(fields[3]),
                        DateTimeParser.parseStoredValue(fields[4]));
                break;
            default:
                throw new IllegalArgumentException("Unknown task type.");
        }

        if (Boolean.parseBoolean(fields[1])) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Ensures that a serialized task has exactly the expected fields.
     *
     * @param fields the parsed fields.
     * @param expectedCount the required number of fields.
     */
    private void requireFieldCount(String[] fields, int expectedCount) {
        if (fields.length != expectedCount) {
            throw new IllegalArgumentException("Unexpected task data.");
        }
    }
}
