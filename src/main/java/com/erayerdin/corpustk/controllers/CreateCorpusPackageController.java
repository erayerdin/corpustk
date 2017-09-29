package com.erayerdin.corpustk.controllers;

import com.erayerdin.corpustk.Utils;
import com.erayerdin.corpustk.core.listcells.GraphSetListCell;
import com.erayerdin.corpustk.models.corpus.Corpus;
import com.erayerdin.corpustk.models.graphology.GraphSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
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
        log.debug("Initializing CreateCorpusPackageController...");

        // load graphsets
        GraphSet[] graphSets = Utils.loadGraphSets();
        this.graphSetListView.getItems().addAll(graphSets);
        this.graphSetListView.setCellFactory(param -> new GraphSetListCell());
        this.graphSetListView.getSelectionModel().selectFirst();
    }

    public boolean validateForm() {
        log.debug(String.format("Validating %s...", getClass().getName()));
        boolean isValid = true;

        if (this.corpusPackageTitleTextField.getText().isEmpty()
                || this.pathTextField.getText().isEmpty()) { // if not valid
            log.error("Form is not valid.");

            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Invalid Form");
            error.setHeaderText("Form is invalid.");
            error.setContentText("Corpus Package Title and Path shouldn't be empty.");
            error.showAndWait();
            isValid = false;
            return isValid;
        }

        return isValid;
    }

    @FXML
    void browseFiles(ActionEvent event) {
        log.debug("Browsing files...");

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("Corpus Packages","*.crp");
        fileChooser.getExtensionFilters().add(ext);
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
        boolean isValid = this.validateForm(); // TODO problem on validation
        // oddly throwing invalidation for form

        if (!isValid) return;

        Corpus corpus = new Corpus(
                this.corpusPackageTitleTextField.getText(),
                this.graphSetListView.getSelectionModel().getSelectedItem()
        );
        File file = new File(this.pathTextField.getText());
        try {
            file.createNewFile();
        } catch (IOException e) {
            log.error("Could not create corpus file...", e);

            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Could Not Create Corpus File");
            error.setHeaderText("Corpus File could not be created.");
            error.setContentText("Please make sure your disk is not damaged or has space.");
            error.showAndWait();
            return;
        }
        corpus.setFileOnDisk(file);

        MainController.setCorpusInstance(corpus);
        this.getStage(event).close();
    }

}
