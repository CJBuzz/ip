package avon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import avon.storage.Storage;
import avon.ui.Ui;

class AvonTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void run_corruptedDataFile_doesNotProcessCommandsOrOverwriteFile() throws IOException {
        Path dataFile = temporaryDirectory.resolve("avon.txt");
        String corruptedData = "D\tfalse\tmissing date";
        Files.writeString(dataFile, corruptedData);
        ByteArrayInputStream input = new ByteArrayInputStream(
                "todo overwrite data\nbye\n".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Avon avon = new Avon(new Ui(input, new PrintStream(output)), new Storage(dataFile));

        avon.run();

        assertEquals(corruptedData, Files.readString(dataFile));
        assertFalse(output.toString(StandardCharsets.UTF_8).contains("I've added this task"));
    }
}
