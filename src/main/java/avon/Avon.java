package avon;

import java.nio.file.Path;

/**
 * Runs the command-line interface for Avon.
 */
public class Avon {
    private static final Path DATA_FILE_PATH = Path.of("data", "avon.txt");

    /**
     * Starts Avon and processes commands until the user exits or input ends.
     *
     * @param args command-line arguments, which Avon does not use.
     */
    public static void main(String[] args) {
        new Avon().run();
    }

    /**
     * Processes commands until the user exits or input ends.
     */
    public void run() {
        Ui ui = new Ui();
        ui.showWelcome();
        Storage storage = new Storage(DATA_FILE_PATH);
        TaskList taskList = loadTasks(storage, ui);
        boolean isExit = false;
        while (!isExit && ui.hasNextCommand()) {
            String fullCommand = ui.readCommand();
            ui.showSeparator();

            try {
                Command command = Parser.parse(fullCommand);
                command.execute(taskList, ui, storage);
                isExit = command.isExit();
            } catch (AvonException exception) {
                ui.showError(exception);
            } finally {
                ui.showSeparator();
            }
        }
        ui.close();
    }

    /**
     * Loads saved tasks, falling back to an empty list if loading fails.
     *
     * @param storage the persistent task storage.
     * @param ui the command-line interface.
     * @return the restored task list.
     */
    private static TaskList loadTasks(Storage storage, Ui ui) {
        try {
            return new TaskList(storage.load());
        } catch (StorageException exception) {
            ui.showError(exception);
            return new TaskList();
        }
    }

}
