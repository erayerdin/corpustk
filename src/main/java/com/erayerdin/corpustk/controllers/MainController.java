package com.erayerdin.corpustk.controllers;

import com.erayerdin.corpustk.Utils;
import com.erayerdin.corpustk.core.listcells.TextListCell;
import com.erayerdin.corpustk.models.Model;
import com.erayerdin.corpustk.models.corpus.Corpus;
import com.erayerdin.corpustk.models.corpus.Text;
import com.erayerdin.corpustk.views.AboutView;
import com.erayerdin.corpustk.views.CreateCorpusPackageView;
import com.erayerdin.corpustk.views.CreateGraphSetView;
import com.erayerdin.corpustk.views.TextView;
import com.erayerdin.linglib.corpus.Query;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Optional;
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

                // change title
                this.getStage().setTitle("Corpus Toolkit - "+newVal.getTitle());

                this.disableCorpusInstanceListeners(false);
//                this.textListener();
//                this.filteredTextListeners();
//                this.queryListener();
                this.textsListView.setItems(getCorpusInstance().getTexts()); // ?
            } else {
                log.debug("Corpus is null. Disabling UI...");
                this.getStage().setTitle("Corpus Toolkit");

                this.textsListView.getItems().clear();
                this.disableCorpusInstanceListeners(true);
            }
        });

        this.textsListView.setCellFactory(param -> new TextListCell());
        this.initializeKeyCombinations();

        // Adding Event Handler to Text ListView
        this.textsListViewEventHandler();
    }

    public void initializeKeyCombinations() {
        log.debug("Initializing key combinations...");

        this.newCorpusPackageMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        this.openCorpusPackageMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        this.saveCorpusPackageMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        this.addGraphSetMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.G, KeyCombination.CONTROL_DOWN));
        this.quitMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
        this.aboutMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F1));
    }

    public void textsListViewEventHandler() {
        log.debug("Adding double-click event handler to Text ListView...");
        this.textsListView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                log.debug("Initializing TextView...");
                Text selected = this.textsListView.getSelectionModel().getSelectedItem();

                log.debug("Setting TextView's text instance...");
                TextController.setTextInstance(selected);

                TextView textView = new TextView();
                Scene scene = null;

                try {
                    scene = textView.createScene();
                } catch (IOException e1) {
                    log.error("An error occured while initializing TextView...", e);
                    System.exit(1);
                }

                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.setTitle(textView.getTitle());
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                // update list

                TextController.setTextInstance(null);
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

    private void textListener() {
        log.debug("Adding listeners for main texts...");

        ListChangeListener textListener = new ListChangeListener() {
            @Override
            public void onChanged(Change c) {
                if (getCorpusInstance().getFilteredTexts().isEmpty()) {
                    log.debug("Filtered texts is empty. Updating Texts ListView to main texts...");
                    textsListView.getItems().clear();
                    textsListView.getItems().addAll(getCorpusInstance().getTexts());
                } else {
                    log.warn("Filtered texts is not empty. Cannot update Texts ListView with main texts...");
                }
                System.out.println();
            }
        };

        try {
            // adding listener to main texts
            corpusInstance.get().getTexts().addListener(textListener);
        } catch (NullPointerException e) {
            log.warn("Corpus is null. Cannot listen main texts.", e);
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
        this.textsListView.setDisable(disabled);
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

    public boolean corpusExistsAlert() {
        log.debug("Checking if corpus already opened...");

        boolean r = false;

        if (getCorpusInstance() != null) {
            log.warn("Corpus is already opened. Creating confirmation alert...");

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Corpus Already Opened");
            alert.setHeaderText("A corpus file is already opened.");
            alert.getDialogPane().setContent(new Label("Do you wish to proceed? If you click OK, then you might lose changes on your current corpus package."));

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                log.warn("User decided to overwrite Corpus instance.");
                r = true;
            } else {
                log.warn("User decided to keep Corpus instance.");
            }
        }

        return r;
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

    public Stage getStage() {
        Stage stage = (Stage) this.resetNgramButton.getScene().getWindow();
        return stage;
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
        log.debug("Importing texts...");

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("Text File", "*.txt");
        fileChooser.getExtensionFilters().add(ext);
        List<File> files = fileChooser.showOpenMultipleDialog(this.getWindow(event));

        if (files != null) {
            files.stream()
                    .forEach(f -> {
                        StringBuilder sb = new StringBuilder();
                        FileReader fr = null;

                        try {
                            fr = new FileReader(f.getAbsolutePath());
                        } catch (FileNotFoundException e) {
                            log.error(e);
                            return;
                        }

                        BufferedReader br = new BufferedReader(fr);

                        String line;
                        try {
                            while ((line = br.readLine()) != null) {
                                sb.append(line+"\n");
                            }
                        } catch (IOException e) {
                            log.error(String.format("An error occured while reading a text: %s", f.getAbsolutePath()), e);
                            return;
                        }

                        String content = sb.toString();

                        Text text = new Text(content, getCorpusInstance().getGraphSet());
                        getCorpusInstance().getTexts().add(text);
                    });
        } else {
            log.warn("No text file selected.");
            return;
        }
    }

    @FXML
    void newCorpusPackage(ActionEvent event) {
        boolean r = this.corpusExistsAlert();
        if (r) return; // TODO patch here

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
        createCorpusPackageStage.show();
    }

    @FXML
    void openCorpusPackage(ActionEvent event) {
        boolean r = this.corpusExistsAlert();
        if (r) return; // TODO patch here

        log.debug("Opening corpus package...");

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("Corpus Package", "*.crp");
        fileChooser.getExtensionFilters().add(ext);
        File file = fileChooser.showOpenDialog(this.getWindow(event));

        if (file != null) {
            Corpus corpus = null;

            try {
                corpus = (Corpus) Model.load(file);
            } catch (IOException e) {
                log.error("An error occured while loading corpus file...", e);

                Utils.generateErrorAlert(
                        "Corpus File Invalid",
                        "Corpus file is not valid.",
                        "Corpus file is invalid or corrupted."
                );
                return;
            } catch (ClassNotFoundException e) {
                log.error("An error occured while deserializing corpus file...", e);

                Utils.generateErrorAlert(
                        "Corpus File Invalid",
                        "Corpus file is not valid.",
                        "Corpus file is invalid or deprecated."
                );
                return;
            }

            corpus.setFileOnDisk(file);
            setCorpusInstance(corpus);
        } else {
            log.warn("User didn't choose any corpus file to open.");
            return;
        }
    }

    @FXML
    void quit(ActionEvent event) {
        boolean r = this.corpusExistsAlert();
        if (!r) return;

        Platform.exit();
    }

    @FXML
    void resetFilter(ActionEvent event) {
        log.debug("Resetting filters...");
        getCorpusInstance().getFilteredTexts().clear();
    }

    @FXML
    void resetNgrams(ActionEvent event) {
        log.debug("Resetting table...");
        getCorpusInstance().getQueries().clear();
    }

    @FXML
    void saveCorpusPackage(ActionEvent event) {
        log.debug("Serializing current corpus instance to a file...");

        try {
            Model.save(getCorpusInstance(), getCorpusInstance().getFileOnDisk());
        } catch (IOException e) {
            log.error("An error occured while serializing corpus instance to a file.");
            Utils.generateErrorAlert(
                    "Could Not Be Saved",
                    "Corpus package could not be saved.",
                    "Please make sure your disk is not damaged or you have permissions to save to the current path."
            );
            return;
        }
    }

    @FXML
    void saveCorpusPackageAs(ActionEvent event) {

    }

    @FXML
    void searchNgrams(ActionEvent event) {

    }

}
