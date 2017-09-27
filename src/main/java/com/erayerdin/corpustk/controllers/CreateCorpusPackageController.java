package com.erayerdin.corpustk.controllers;

import com.erayerdin.corpustk.Utils;
import com.erayerdin.corpustk.models.corpus.Corpus;
import com.erayerdin.corpustk.models.graphology.GraphSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
public class CreateCorpusPackageController extends Controller implements Form {

    @FXML
    private TextField pathTextField;

    @FXML
    private Button browseFilesButton;

    @FXML
    private TextField corpusPackageTitleTextField;

    @FXML
    private ListView<GraphSet> graphSetListView;

    @FXML
    private Button createCorpusPackageButton;

    @FXML
    private Button cancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // load graphsets
        GraphSet[] graphSets = Utils.loadGraphSets();
        this.graphSetListView.getItems().addAll(graphSets);
        this.graphSetListView.setCellFactory(param -> new ListCell<GraphSet>() {
            @Override
            protected void updateItem(GraphSet item, boolean empty) {
                super.updateItem(item, empty);
                setText(item.getTitle()); // set title of graphsets
            }
        });
    }

    public void validateForm() {
        log.debug(String.format("Validating %s...", getClass().getName()));

        if (!(this.corpusPackageTitleTextField.getText().isEmpty()
                && this.pathTextField.getText().isEmpty())) { // if not valid
            log.error("Form is not valid.");

            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Invalid Form");
            error.setHeaderText("Form is invalid.");
            error.setContentText("Corpus Package Title and Path shouldn't be empty.");
            error.showAndWait();
        }
    }

    @FXML
    void browseFiles(ActionEvent event) {
        log.debug("Browsing files...");

        FileChooser fileChooser = new FileChooser();
        File saveFile = fileChooser.showSaveDialog(this.getWindow(event));

        if (saveFile == null) {
            log.debug("Save file not selected.");

            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("File Not Defined");
            error.setHeaderText("File was not defined.");
            error.setContentText("Please define a file to save the corpus.");
            error.showAndWait();
            return;
        } else {
            this.pathTextField.setText(saveFile.getAbsolutePath());
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        this.getStage(event).close();
    }

    @FXML
    void createCorpusPackage(ActionEvent event) {
        log.debug("Creating corpus object...");
        this.validateForm();

        Corpus corpus = new Corpus(
                this.corpusPackageTitleTextField.getText(),
                this.graphSetListView.getSelectionModel().getSelectedItem()
        );
        corpus.setFileOnDisk(new File(this.pathTextField.getText()));

        MainController.setCorpusInstance(corpus);
    }

}
