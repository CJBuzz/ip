package avon.gui;

import avon.Avon;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controls Avon's primary chat window.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    private Avon avon;

    /**
     * Keeps the latest dialog visible and displays Avon's greeting.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        dialogContainer.getChildren().add(DialogBox.getAvonDialog(
                "Hark! I am Avon. How may my hand or wit now serve thy need?"));
    }

    /**
     * Supplies the application logic used to answer user commands.
     *
     * @param avon the Avon command engine.
     */
    public void setAvon(Avon avon) {
        this.avon = avon;
    }

    /**
     * Sends the entered command to Avon and appends both sides of the exchange.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = avon.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input),
                DialogBox.getAvonDialog(response));
        userInput.clear();

        if (input.strip().equals("bye")) {
            Platform.exit();
        }
    }
}
