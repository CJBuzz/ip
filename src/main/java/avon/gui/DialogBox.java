package avon.gui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Displays one speaker label and one message in the conversation.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;

    @FXML
    private Label speaker;

    private DialogBox(String text, String speakerName) {
        try {
            FXMLLoader loader = new FXMLLoader(DialogBox.class.getResource("/view/DialogBox.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load the dialog box view.", exception);
        }
        dialog.setText(text);
        speaker.setText(speakerName);
    }

    /**
     * Creates a right-aligned dialog for the user.
     *
     * @param text the user's command.
     * @return the user dialog.
     */
    public static DialogBox getUserDialog(String text) {
        return new DialogBox(text, "THEE");
    }

    /**
     * Creates a left-aligned dialog for Avon.
     *
     * @param text Avon's response.
     * @return Avon's dialog.
     */
    public static DialogBox getAvonDialog(String text) {
        DialogBox dialogBox = new DialogBox(text, "AVON");
        dialogBox.flip();
        dialogBox.getStyleClass().add("avon-dialog");
        return dialogBox;
    }

    /**
     * Places the speaker label before the response and aligns it to the left.
     */
    private void flip() {
        ObservableList<Node> children = FXCollections.observableArrayList(getChildren());
        Collections.reverse(children);
        getChildren().setAll(children);
        setAlignment(Pos.TOP_LEFT);
    }
}
