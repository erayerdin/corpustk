package com.erayerdin.corpustk.controllers;

import com.erayerdin.corpustk.App;
import com.erayerdin.corpustk.core.corpus.Corpus;
import com.erayerdin.corpustk.core.corpus.TextInstance;
import com.erayerdin.corpustk.core.graphology.GraphSetInstance;
import com.erayerdin.corpustk.scenes.AboutScene;
import com.erayerdin.corpustk.scenes.CreateCorpusPackageScene;
import com.erayerdin.corpustk.scenes.CreateGraphSetScene;
import com.erayerdin.linglib.corpus.Text;
import com.erayerdin.linglib.graphology.GraphSetFactory;
import com.erayerdin.linglib.graphology.Grapheme;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Log4j2
public class MainController implements Initializable {

    @FXML
    private MenuItem newCorpusPackageMenuItem;

    @FXML
    private MenuItem openCorpusPackageMenuItem;

    @FXML
    private MenuItem saveCorpusPackageMenuItem;

    @FXML
    private MenuItem saveCorpusPackageAsMenuItem;

    @FXML
    private MenuItem addGraphSetMenuItem;

    @FXML
    private MenuItem quitMenuItem;

    @FXML
    private MenuItem aboutMenuItem;

    @FXML
    private Button newCorpusPackageButton;

    @FXML
    private Button openCorpusPackageButton;

    @FXML
    private Button saveCorpusPackageButton;

    @FXML
    private Button importTextButton;

    @FXML
    private ChoiceBox<?> graphSetChoiceBox;

    @FXML
    private Button exportAsTable;

    @FXML
    private TextField ngramQueryTextField;

    @FXML
    private ChoiceBox<?> ngramTypeChoiceBox;

    @FXML
    private ChoiceBox<?> ngramQueryTypeChoiceBox;

    @FXML
    private Button ngramSearchButton;

    @FXML
    private Button resetNgramButton;

    @FXML
    private TableView<?> ngramsTableView;

    @FXML
    private TextField textFilterQueryTextField;

    @FXML
    private Button textFilterButton;

    @FXML
    private Button textFilterResetButton;

    @FXML
    private ChoiceBox<?> textFilterTypeChoiceBox;

    @FXML
    private ListView<TextInstance> textsListView;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("Initializing bindings for corpus instance...");

        App.corpusObjProperty().addListener((prop, oldVal, newVal) -> {
            if (newVal == null) {
                this.setCorpusBindings(true, null);
            } else {
                this.setCorpusBindings(false, newVal.getTitle());
                App.setTextInstances(newVal.getTextInstances());
                this.textsListView.getItems().addAll(App.getTextInstances());
            }
        });


    }

    private void setCorpusBindings(boolean disabled, String title) {
        log.debug("Setting corpus binding items' disabled property to "+Boolean.toString(disabled));
        this.saveCorpusPackageMenuItem.setDisable(disabled);
        this.saveCorpusPackageAsMenuItem.setDisable(disabled);
        this.saveCorpusPackageButton.setDisable(disabled);
        this.importTextButton.setDisable(disabled);
        this.graphSetChoiceBox.setDisable(disabled);
        this.exportAsTable.setDisable(disabled);
        this.textFilterQueryTextField.setDisable(disabled);
        this.textFilterTypeChoiceBox.setDisable(disabled);
        this.textFilterButton.setDisable(disabled);
        this.textFilterResetButton.setDisable(disabled);
        this.textsListView.setDisable(disabled);
        this.ngramsTableView.setDisable(disabled);
        this.ngramQueryTextField.setDisable(disabled);
        this.ngramTypeChoiceBox.setDisable(disabled);
        this.ngramQueryTypeChoiceBox.setDisable(disabled);
        this.ngramSearchButton.setDisable(disabled);
        this.resetNgramButton.setDisable(disabled);

        log.debug("Changing App title...");
        if (title == null) App.getStage().setTitle("Corpus Toolkit");
        else App.getStage().setTitle("Corpus Toolkit - "+title);
    }

    @FXML
    void about(ActionEvent event) {
        Stage stage = new Stage();
        stage.initOwner(App.getStage());
        stage.initModality(Modality.APPLICATION_MODAL);

        AboutScene aboutScene = new AboutScene();
        stage.setScene(aboutScene.getScene());
        stage.setResizable(false);
        stage.setTitle(aboutScene.getTitle());

        stage.setOnCloseRequest(e -> {
            log.debug("Closing AboutScene stage...");
            stage.close();
        });
        stage.showAndWait();
    }

    @FXML
    void addGraphSet(ActionEvent event) {
        Stage stage = new Stage();
        stage.initOwner(App.getStage());
        stage.initModality(Modality.APPLICATION_MODAL);

        CreateGraphSetScene scene = new CreateGraphSetScene();
        stage.setScene(scene.getScene());
        stage.setResizable(false);
        stage.setTitle(scene.getTitle());

        stage.setOnCloseRequest(e -> {
            log.debug("Closing CreateGraphSetScene stage...");
            stage.close();
        });
        stage.showAndWait();
    }

    @FXML
    void exportAsTable(ActionEvent event) {

    }

    @FXML
    void filterTexts(ActionEvent event) {

    }

    @FXML
    void importText(ActionEvent event) {
        log.debug("Loading import text FileChooser...");
        FileChooser importFiles = new FileChooser();

        log.debug("Adding *.txt extension...");
        FileChooser.ExtensionFilter textExtFilter = new FileChooser.ExtensionFilter("(*.txt) Text files", "*.txt");
        importFiles.getExtensionFilters().add(textExtFilter);

        importFiles.setTitle("Import Text Files");
        log.debug("Getting files...");
        List<File> textFiles = importFiles.showOpenMultipleDialog(this.getStage(event))
                .stream()
                .filter(o -> {
                    log.debug(String.format("Filtering %s", o.getAbsolutePath()));
                    return o.getAbsolutePath().endsWith(".txt");
                }).collect(Collectors.toList()); // filter to txt

        log.debug("Creating TextInstances from textFiles...");
        List<TextInstance> textInstancesList = textFiles.stream()
                .map(o -> {
                    log.debug(String.format("Creating TextInstance of %s", o.getAbsolutePath()));
                    String textString = null;
                    try {
                        textString = new String(Files.readAllBytes(Paths.get(o.getAbsolutePath())));
                    } catch (IOException e) {
                        log.error(String.format("An error occured while reading file %s", o.getAbsolutePath()), e);
                        return null;
                    }
                    TextInstance textInstance = new TextInstance(new Text(textString), App.getCurrentGraphSetInstance());
                    return textInstance;
                }).filter(o -> o != null)
                .collect(Collectors.toList());

        log.debug("Adding created text instances to the list.");
        App.getTextInstances().addAll(textInstancesList);
    }

    @FXML
    void newCorpusPackage(ActionEvent event) {
        Stage stage = new Stage();
        stage.initOwner(App.getStage());
        stage.initModality(Modality.APPLICATION_MODAL);

        CreateCorpusPackageScene scene = new CreateCorpusPackageScene();
        stage.setScene(scene.getScene());
        stage.setResizable(false);
        stage.setTitle(scene.getTitle());

        stage.setOnCloseRequest(e -> {
            log.debug("Closing CreateCorpusPackageScene...");
            stage.close();
        });
        stage.showAndWait();
    }

    @FXML
    void openCorpusPackage(ActionEvent event) {

    }

    @FXML
    void quit(ActionEvent event) {
        log.debug("Exiting application...");
        System.exit(0);
    }

    @FXML
    void resetFilter(ActionEvent event) {

    }

    @FXML
    void resetNgrams(ActionEvent event) {

    }

    @FXML
    void saveCorpusPackage(ActionEvent event) {

    }

    @FXML
    void saveCorpusPackageAs(ActionEvent event) {

    }

    @FXML
    void searchNgrams(ActionEvent event) {

    }

}

