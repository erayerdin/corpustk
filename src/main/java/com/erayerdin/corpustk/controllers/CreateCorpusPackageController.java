package com.erayerdin.corpustk.controllers;

import com.erayerdin.corpustk.App;
import com.erayerdin.corpustk.core.corpus.Corpus;
import com.erayerdin.corpustk.core.graphology.GraphSetInstance;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
public class CreateCorpusPackageController implements Initializable {

    @FXML
    private TextField pathTextField;

    @FXML
    private Button browseFilesButton;

    @FXML
    private TextField corpusPackageTitleTextField;

    @FXML
    private ListView<GraphSetInstance> graphSetListView;

    @FXML
    private Button createCorpusPackageButton;

    @FXML
    private Button cancelButton;

    private File file = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // load graphsetinstances
        GraphSetInstance[] graphSetInstances = App.getGraphSetInstances();
        this.graphSetListView.getItems().addAll(graphSetInstances);
    }

    private Stage getStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        return stage;
    }

    private void closeCurrentWindow(ActionEvent event) {
        // https://stackoverflow.com/a/11476162/2926992
        Stage stage = this.getStage(event);
        stage.close();
    }

    @FXML
    void browseFiles(ActionEvent event) {
        log.debug("Browsing files to save...");
        FileChooser saveDialog = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Corpus File (*.crp)", "*.crp");
        saveDialog.getExtensionFilters().add(extFilter);

        File file = saveDialog.showSaveDialog(this.getStage(event));

        if (file != null) {
            log.debug(String.format("Selected path to save: %s", file.getAbsolutePath()));
            this.pathTextField.setText(file.getAbsolutePath());
            this.file = file;
        } else {
            log.warn("File to save was not selected.");
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        this.closeCurrentWindow(event);
    }

    @FXML
    void createCorpusPackage(ActionEvent event) {
        if (this.corpusPackageTitleTextField.getText() == null || this.corpusPackageTitleTextField.getText() == "") {
            log.error("No title.");
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("No Title");
            error.setHeaderText("There is no title.");
            error.setContentText("Please give title to your corpus package.");
            error.showAndWait();
            return;
        }

        if (this.file == null) {
            log.error("File was not selected, so cannot be created.");
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("File Not Selected");
            error.setHeaderText("File was not selected.");
            error.setContentText("File was not selected, so it cannot be saved. Please select/create new corpus package file by browsing.");
            error.showAndWait();
            return;
        }

        try {
            log.debug(String.format("Creating file: %s", this.file.getAbsolutePath()));
            this.file.createNewFile();
        } catch (IOException e) {
            log.error("An error occured while saving corpus file...", e);
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle(e.getClass().getName());
            error.setHeaderText("File could not be saved.");
            error.setContentText("File could not be saved. Please be sure your disk is not damaged or full.");
            error.showAndWait();
            return;
        }

        App.setCorpusFile(this.file);
        App.setCorpusObj(new Corpus(this.corpusPackageTitleTextField.getText(), this.graphSetListView.getSelectionModel().getSelectedItem()));
        this.closeCurrentWindow(event);
    }
}
