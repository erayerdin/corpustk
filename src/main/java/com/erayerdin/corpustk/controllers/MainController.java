package com.erayerdin.corpustk.controllers;

import com.erayerdin.corpustk.models.corpus.Corpus;
import com.erayerdin.corpustk.models.corpus.Text;
import com.erayerdin.corpustk.views.AboutView;
import com.erayerdin.corpustk.views.CreateCorpusPackageView;
import com.erayerdin.corpustk.views.CreateGraphSetView;
import com.erayerdin.linglib.corpus.Query;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
public class MainController extends Controller {

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
    private TableView<Query> ngramsTableView;

    @FXML
    private TextField textFilterQueryTextField;

    @FXML
    private Button textFilterButton;

    @FXML
    private Button textFilterResetButton;

    @FXML
    private ChoiceBox<?> textFilterTypeChoiceBox;

    @FXML
    private ListView<Text> textsListView;

    //////////////////
    // Model Fields //
    //////////////////

    private static ObjectProperty<Corpus> corpusInstance;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug(String.format("Initializing %s...", this.getClass().getName()));

        corpusInstance = new SimpleObjectProperty<>(null);

        corpusInstance.addListener((prop, oldVal, newVal) -> {
            if (newVal != null) {
                log.debug("Corpus initialized. Adding listeners...");
                this.disableCorpusInstanceListeners(false);
                this.filteredTextListeners();
                this.queryListener();
            } else {
                log.debug("Corpus is null. Disabling UI...");
                this.disableCorpusInstanceListeners(true);
            }
        });
    }

    ///////////////////
    // Model Methods //
    ///////////////////

    //// Listeners ////
    private void queryListener() {
        log.debug("Adding listeners for queries...");

        ListChangeListener queryListener = new ListChangeListener() {
            @Override
            public void onChanged(Change c) {
                ObservableList<Query> queries = c.getList();
                ngramsTableView.getItems().clear();

                log.debug("Query found. Updating query table view to queries.");
                // TODO implement how the table will look like
                ngramsTableView.getItems().addAll(queries);
            }
        };

        try {
            corpusInstance.get().getQueries().addListener(queryListener);
        } catch (NullPointerException e) {
            log.warn("Corpus is null. Cannot listen queries.");
        }
    }

    private void filteredTextListeners() {
        log.debug("Adding listeners for filtered texts...");

        ListChangeListener filteredTextListener = new ListChangeListener() {
            @Override
            public void onChanged(Change c) {
                ObservableList<Text> filteredTexts = c.getList();
                textsListView.getItems().clear();

                // if there are filtered texts
                if (!filteredTexts.isEmpty()) {
                    log.debug("Filtered texts found. Updating text list view to filtered texts.");
                    textsListView.getItems().setAll(filteredTexts);
                } else {
                    log.debug("Filtered texts got reset. Updating text list view to corpus texts.");
                    textsListView.getItems().setAll(corpusInstance.get().getTexts());
                }
            }
        };

        try {
            // adding listener to textsListView
            corpusInstance.get().getFilteredTexts().addListener(filteredTextListener);
        } catch (NullPointerException e) {
            log.warn("Corpus is null. Cannot listen filtered texts.", e);
        }
    }

    //// Other Methods ////

    private void disableCorpusInstanceListeners(boolean disabled) {
        log.debug(String.format("Setting corpus instance listener elements to %s", Boolean.toString(disabled)));

        // View
        this.saveCorpusPackageButton.setDisable(disabled);
        this.importTextButton.setDisable(disabled);
        this.graphSetChoiceBox.setDisable(disabled);
        this.exportAsTable.setDisable(disabled);
        this.textFilterQueryTextField.setDisable(disabled);
        this.textFilterTypeChoiceBox.setDisable(disabled);
        this.textFilterButton.setDisable(disabled);
        this.textFilterResetButton.setDisable(disabled);
        this.ngramQueryTextField.setDisable(disabled);
        this.ngramTypeChoiceBox.setDisable(disabled);
        this.ngramQueryTypeChoiceBox.setDisable(disabled);
        this.ngramSearchButton.setDisable(disabled);
        this.resetNgramButton.setDisable(disabled);
        this.ngramsTableView.setDisable(disabled);

        // Menu Elements
        this.saveCorpusPackageMenuItem.setDisable(disabled);
        this.saveCorpusPackageAsMenuItem.setDisable(disabled);
    }

    public static Corpus getCorpusInstance() {
        return corpusInstance.get();
    }

    public static ObjectProperty<Corpus> corpusInstanceProperty() {
        return corpusInstance;
    }

    public static void setCorpusInstance(Corpus corpusInstance) {
        MainController.corpusInstance.set(corpusInstance);
    }

    ///////////////////////////

    @FXML
    void about(ActionEvent event) {
        AboutView aboutView = new AboutView();
        Scene aboutScene = null;
        try {
            aboutScene = aboutView.createScene();
        } catch (IOException e) {
            log.error(String.format("An error occured while loading %s.", aboutView.getTitle()), e);
            System.exit(1);
        }

        Stage aboutStage = new Stage();
        aboutStage.setTitle(aboutView.getTitle());
        aboutStage.setScene(aboutScene);
        aboutStage.setResizable(false);
        aboutStage.initModality(Modality.APPLICATION_MODAL);
        aboutStage.show();
    }

    @FXML
    void addGraphSet(ActionEvent event) {
        CreateGraphSetView gsView = new CreateGraphSetView();
        Scene gsScene = null;

        try {
            gsScene = gsView.createScene();
        } catch (IOException e) {
            log.error("An error occured while loading CreateGraphSetView.", e);
            System.exit(1);
        }

        Stage gsStage = new Stage();
        gsStage.setTitle(gsView.getTitle());
        gsStage.setScene(gsScene);
        gsStage.setResizable(false);
        gsStage.initModality(Modality.APPLICATION_MODAL);
        gsStage.show();
    }

    @FXML
    void exportAsTable(ActionEvent event) {

    }

    @FXML
    void filterTexts(ActionEvent event) {

    }

    @FXML
    void importText(ActionEvent event) {

    }

    @FXML
    void newCorpusPackage(ActionEvent event) {
        CreateCorpusPackageView createCorpusPackageView = new CreateCorpusPackageView();
        Scene createCorpusPackageScene = null;

        try {
            createCorpusPackageScene = createCorpusPackageView.createScene();
        } catch (IOException e) {
            log.error(String.format("An error occured while loading %s.", createCorpusPackageView.getTitle()), e);
            System.exit(1);
        }

        Stage createCorpusPackageStage = new Stage();
        createCorpusPackageStage.setScene(createCorpusPackageScene);
        createCorpusPackageStage.setTitle(createCorpusPackageView.getTitle());
        createCorpusPackageStage.setResizable(false);
        createCorpusPackageStage.initModality(Modality.APPLICATION_MODAL);
        createCorpusPackageStage.show(); // TODO get data from this stage
    }

    @FXML
    void openCorpusPackage(ActionEvent event) {

    }

    @FXML
    void quit(ActionEvent event) {
        Platform.exit();
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
