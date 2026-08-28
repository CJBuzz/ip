package avon.gui;

import java.io.IOException;

import avon.Avon;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Displays Avon's JavaFX user interface.
 */
public class Main extends Application {
    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
        AnchorPane root = loader.load();
        MainWindow mainWindow = loader.getController();
        mainWindow.setAvon(new Avon());

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Main.class.getResource("/view/avon.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Avon");
        stage.setMinHeight(600);
        stage.setMinWidth(420);
        stage.show();
    }
}
