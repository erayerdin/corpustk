package com.erayerdin.corpustk.controllers;

import com.erayerdin.corpustk.Utils;
import com.erayerdin.corpustk.models.corpus.Text;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
public class TextController extends Controller implements Form {

    @FXML
    private TextArea contentTextArea;

    @FXML
    private ListView<String> tagsListView;

    @FXML
    private Button removeTagButton;

    @FXML
    private TextField tagTextField;

    @FXML
    private Button addTagButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    // Model //
    private static ObjectProperty<Text> textInstance = new SimpleObjectProperty<Text>(null);

    ///////////////

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("Initializing Text controller...");

        // Setting text
        this.contentTextArea.setText(getTextInstance().getContent());

        // Adding tags
        String[] tags = getTextInstance().getTags().toArray(new String[getTextInstance().getTags().size()]);
        this.tagsListView.getItems().addAll(tags);

        // text instance will be nullified in MainController
    }

    @Override
    public boolean validateForm() {
        log.debug("Validating form...");
        boolean r = true;

        if (this.contentTextArea.getText().isEmpty())
            r = false;

        // Validation Failed

        if (!r) {
            Utils.generateErrorAlert(
                    "Text Invalid",
                    "Text is not valid.",
                    "Content must be filled."
            );
        }

        log.debug(String.format("Is form valid? %b", r));
        return r;
    }

    // Getters and Setters //

    public Stage getStage() {
        Stage stage = (Stage) this.cancelButton.getScene().getWindow();
        return stage;
    }

    public static Text getTextInstance() {
        return textInstance.get();
    }

    public static ObjectProperty<Text> textInstanceProperty() {
        return textInstance;
    }

    public static void setTextInstance(Text textInstance) {
        TextController.textInstance.set(textInstance);
    }


    ///////////////

    @FXML
    void addTag(ActionEvent event) {
        String tag = this.tagTextField.getText().trim();
        log.debug(String.format("Adding tag %s...", tag));

        // validate tag
        boolean isValid = tag.chars().allMatch(c -> Character.isAlphabetic(c) || Character.isDigit(c) || c == '-');

        if (!isValid) {
            log.warn(String.format("%s is invalid as tag. Returning...", tag));
            Utils.generateErrorAlert(
                    "Invalid Tag",
                    "Tag is not valid.",
                    "Tags can only contain letters, numbers and dash (-) character."
            );
            return;
        }

        if (tag.isEmpty()) {
            log.warn("Tag is trimmed and resulted empty, clearing field and returning...");
            this.tagTextField.clear();
            return;
        }

        this.tagsListView.getItems().add(tag);
        this.tagTextField.clear();
    }

    @FXML
    void cancel(ActionEvent event) {
        this.getStage(event).close();
    }

    @FXML
    void removeTag(ActionEvent event) {
        int selected = this.tagsListView.getSelectionModel().getSelectedIndex();
        log.debug(String.format("Removing %s from tags...", this.tagsListView.getItems().get(selected)));
        this.tagsListView.getItems().remove(selected);
    }

    @FXML
    void save(ActionEvent event) {
        log.debug("Saving text...");

        if (!this.validateForm()) return;

        // Update Content
        String text = this.contentTextArea.getText();
        getTextInstance().setContent(text);

        // Update Tags
        getTextInstance().getTags().clear();
        getTextInstance().getTags().addAll(this.tagsListView.getItems());

        this.getStage().close();
    }

}
