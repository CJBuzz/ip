package avon.storage;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
import avon.util.StorageFieldCodec;

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
                    addLoadedTask(tasks, parseTask(line));
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
        Path temporaryFile = null;
        try {
            Path absoluteFilePath = filePath.toAbsolutePath();
            Path parent = absoluteFilePath.getParent();
            Files.createDirectories(parent);
            List<String> lines = new ArrayList<>();
            for (int index = 0; index < taskList.size(); index++) {
                lines.add(serializeTask(taskList.getTask(index)));
            }
            temporaryFile = Files.createTempFile(parent,
                    absoluteFilePath.getFileName().toString(), ".tmp");
            Files.write(temporaryFile, lines);
            replaceDataFile(temporaryFile, absoluteFilePath);
        } catch (IOException | IllegalArgumentException exception) {
            throw new StorageException("I could not preserve thy tasks upon the disk.");
        } finally {
            deleteTemporaryFile(temporaryFile);
        }
    }

    /**
     * Adds a restored task after checking that the data file contains no duplicate record.
     *
     * @param tasks the tasks restored so far.
     * @param task the next task restored from the data file.
     * @throws IllegalArgumentException if an equivalent task was already restored.
     */
    private void addLoadedTask(List<Task> tasks, Task task) {
        boolean isDuplicate = tasks.stream()
                .anyMatch(storedTask -> storedTask.hasSameDetailsAs(task));
        if (isDuplicate) {
            throw new IllegalArgumentException("Duplicate task data.");
        }
        tasks.add(task);
    }

    /**
     * Replaces the data file atomically where the file system supports atomic moves.
     *
     * @param temporaryFile the completely written replacement file.
     * @param absoluteFilePath the destination data file.
     * @throws IOException if neither an atomic nor a regular replacement succeeds.
     */
    private void replaceDataFile(Path temporaryFile, Path absoluteFilePath) throws IOException {
        try {
            Files.move(temporaryFile, absoluteFilePath,
                    StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException exception) {
            Files.move(temporaryFile, absoluteFilePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * Removes an abandoned temporary save file without masking the original save outcome.
     *
     * @param temporaryFile the temporary file, or {@code null} if none was created.
     */
    private void deleteTemporaryFile(Path temporaryFile) {
        if (temporaryFile == null) {
            return;
        }
        try {
            Files.deleteIfExists(temporaryFile);
        } catch (IOException exception) {
            // A stale temporary file is safer than hiding the original save error.
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
                task = new Todo(StorageFieldCodec.unescape(fields[2]));
                break;
            case "D":
                requireFieldCount(fields, 4);
                task = new Deadline(StorageFieldCodec.unescape(fields[2]),
                        DateTimeParser.parseStoredValue(fields[3]));
                break;
            case "E":
                requireFieldCount(fields, 5);
                task = new Event(StorageFieldCodec.unescape(fields[2]),
                        DateTimeParser.parseStoredValue(fields[3]),
                        DateTimeParser.parseStoredValue(fields[4]));
                break;
            default:
                throw new IllegalArgumentException("Unknown task type.");
        }

        if (parseIsDone(fields[1])) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Converts a task into Avon's current tab-separated storage format.
     *
     * @param task the task to serialize.
     * @return the serialized task record.
     * @throws IllegalArgumentException if the task subtype is unsupported.
     */
    private String serializeTask(Task task) {
        String escapedDescription = StorageFieldCodec.escape(task.getDescription());
        if (task instanceof Todo) {
            return "T\t" + task.isDone() + "\t" + escapedDescription;
        }
        if (task instanceof Deadline deadline) {
            return "D\t" + task.isDone() + "\t" + escapedDescription
                    + "\t" + deadline.getBy();
        }
        if (task instanceof Event event) {
            return "E\t" + task.isDone() + "\t" + escapedDescription
                    + "\t" + event.getFrom() + "\t" + event.getTo();
        }
        throw new IllegalArgumentException("Unsupported task type.");
    }

    /**
     * Parses a stored completion flag without silently accepting corrupted values.
     *
     * @param value the stored boolean text.
     * @return the parsed completion state.
     * @throws IllegalArgumentException if the value is not {@code true} or {@code false}.
     */
    private boolean parseIsDone(String value) {
        if (value.equals("true")) {
            return true;
        }
        if (value.equals("false")) {
            return false;
        }
        throw new IllegalArgumentException("Invalid completion state.");
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
