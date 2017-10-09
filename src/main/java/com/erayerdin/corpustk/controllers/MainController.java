package com.erayerdin.corpustk.controllers;

import com.erayerdin.corpustk.App;
import com.erayerdin.corpustk.Utils;
import com.erayerdin.corpustk.core.listcells.TextListCell;
import com.erayerdin.corpustk.models.Model;
import com.erayerdin.corpustk.models.corpus.Corpus;
import com.erayerdin.corpustk.models.corpus.GramType;
import com.erayerdin.corpustk.models.corpus.Text;
import com.erayerdin.corpustk.models.graphology.GraphSet;
import com.erayerdin.corpustk.views.AboutView;
import com.erayerdin.corpustk.views.CreateCorpusPackageView;
import com.erayerdin.corpustk.views.CreateGraphSetView;
import com.erayerdin.corpustk.views.TextView;
import com.erayerdin.linglib.corpus.Query;
import com.erayerdin.linglib.corpus.QueryType;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

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
    private ChoiceBox<GraphSet> graphSetChoiceBox;

    @FXML
    private Button exportAsTable;

    @FXML
    private TextField ngramQueryTextField;

    @FXML
    private ChoiceBox<GramType> ngramTypeChoiceBox;

    @FXML
    private ChoiceBox<QueryType> ngramQueryTypeChoiceBox;

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
    private ChoiceBox<QueryType> textFilterTypeChoiceBox;

    @FXML
    private ListView<Text> textsListView;

    @FXML
    private MenuBar menuBar;

    @FXML
    private ListView<Text> filteredTextsListView;

    //////////////////
    // Model Fields //
    //////////////////

    private static ObjectProperty<Corpus> corpusInstance;

    @Getter @Setter private static FilteredList<Text> filteredTexts;
    @Getter private static ObservableList<Query> queries;
    @Getter @Setter private static File fileOnDisk;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug(String.format("Initializing %s...", this.getClass().getName()));

        final String os = System.getProperty("os.name");
        if (os != null && (os.startsWith("Mac") || os.startsWith("Linux"))) {
            Platform.runLater(() -> menuBar.setUseSystemMenuBar(true));
            log.debug("Using native menu..."); // TODO implement native menu
            // doesn't work on linux
        }

        corpusInstance = new SimpleObjectProperty<>(null);
        this.filteredTexts = new FilteredList<Text>(getCorpusInstance().getTexts(), p -> true);
        this.queries = FXCollections.observableArrayList();
        this.fileOnDisk = null;

        // Add Listeners
        this.corpusNodeListener();
        this.textListener();
        this.filteredTextListener();

        this.textsListView.setCellFactory(param -> new TextListCell());
        this.filteredTextsListView.setCellFactory(param -> new TextListCell());
        this.initializeKeyCombinations();

        // Adding Event Handler to Text ListView
        this.textsListViewEventHandler();

        // Adding enum values to various sections
        this.initializeTypeChoiceBoxes();
    }

    public void checkGraphSet(GraphSet gset) {
        log.debug(String.format("Checking all graphsets for %s...", gset.toString()));

        GraphSet[] gsets = Utils.loadGraphSets();
        boolean exists = false;

        for (GraphSet g : gsets) {
            if (g.equals(gset)) exists = true;
        }

        if (exists) {
            log.debug("GraphSet found, adding items to ChoiceBox...");
            this.graphSetChoiceBox.getItems().addAll(gsets);
            this.graphSetChoiceBox.getSelectionModel().select(gset);
        } else {
            log.warn("GraphSet not found.");

            File gsetFile = new File(new File(App.getUserDataDir(), "graphsets"), gset.getTitle().toLowerCase()+".gset");
            try {
                log.debug("Saving unexpected GraphSet file...");
                Model.save(gset, gsetFile);
            } catch (IOException e) {
                log.error(String.format("An error occured while saving new GraphSet file..."), e);
            }

            gsets = Utils.loadGraphSets();
            this.graphSetChoiceBox.getItems().setAll(gsets);

            try {
                this.graphSetChoiceBox.getSelectionModel().select(gset);
            } catch (Exception e) {
                log.info("Due to error on saving unexpected GraphSet, selecting the first one...");
                this.graphSetChoiceBox.getSelectionModel().selectFirst();
            }
        }
    }

    public void initializeTypeChoiceBoxes() {
        log.debug("Adding QueryType values to Text Filter section...");
        this.textFilterTypeChoiceBox.getItems().addAll(QueryType.values());
        this.textFilterTypeChoiceBox.getSelectionModel().selectFirst();

        log.debug("Adding QueryType values to Ngram section...");
        this.ngramQueryTypeChoiceBox.getItems().addAll(QueryType.values());
        this.ngramQueryTypeChoiceBox.getSelectionModel().selectFirst();

        log.debug("Adding GramType values to Ngram section...");
        this.ngramTypeChoiceBox.getItems().addAll(GramType.values());
        this.ngramTypeChoiceBox.getSelectionModel().selectFirst();
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
    private void corpusNodeListener() {
        ChangeListener corpusNodeListener = (prop, oldCorpus, newCorpus) -> {
            if (newCorpus != null) {
                disableCorpusNodes(false);
            } else {
                disableCorpusNodes(true);
            }
        };

        log.debug("Adding corpus node listener...");
        corpusInstance.addListener(corpusNodeListener);
    }

    private void textListener() {
        ListChangeListener<Text> textListener = (c) -> {
            ObservableList<Text> texts = (ObservableList<Text>) c.getList();
            log.debug("Setting texts to Text ListView...");
            textsListView.setItems(texts);
        };

        corpusInstance.addListener((prop, oldCorpus, newCorpus) -> {
            if (newCorpus != null) {
                log.debug("Adding text listener...");
                textsListView.setItems(filteredTexts);
                filteredTexts.addListener(textListener);
            } else {
                log.debug("Removing text listener...");
                textsListView.getItems().clear();
                filteredTexts.removeListener(textListener);
            }
        });
    }

    private void filteredTextListener() {
        log.debug("Adding listener to text filter query text field...");
        this.textFilterQueryTextField.textProperty().addListener((obs, oldVal, newVal) -> {
            log.debug("Adding predicate to filtered texts...");
            filteredTexts.setPredicate(text -> {
                if (newVal == null || newVal.isEmpty() || newVal.length() < 2) {
                    log.debug("Filter query is null, empty or its length is less than 2. Returning all texts...");
                    return true;
                }

                boolean r = false;

                switch (this.textFilterTypeChoiceBox.getSelectionModel().getSelectedItem()) {
                    case STRING:
                        r = this.isTextFilterValidByString(text);
                        break;
                    case PATTERN:
                        r = this.isTextFilterValidByRegex(text);
                        break;
                    default:
                        break;
                }

                return r;
            });
        });
    }

    private boolean isTextFilterValidByString(Text text) {
        log.debug(String.format("Is %s valid for %s?", text.toString(), this.textFilterQueryTextField.getText().trim()));
        return text.getTags().stream().anyMatch(t -> this.textFilterQueryTextField.getText().trim() == t);
    }

    private boolean isTextFilterValidByRegex(Text text) {
        log.debug(String.format("Is %s valid for [%s] regex?", text.toString(), this.textFilterQueryTextField.getText()));
        Pattern p = Pattern.compile(this.textFilterQueryTextField.getText());
        return text.getTags().stream()
                .anyMatch(t -> p.matcher(t).matches());
    }

    //// Other Methods ////

    private void disableCorpusNodes(boolean disabled) {
        log.debug(String.format("Setting corpus nodes to %s", Boolean.toString(disabled)));

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
        this.filteredTextsListView.setDisable(disabled);

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
        Stage aboutStage = aboutView.createStage();
        aboutStage.show();
    }

    @FXML
    void addGraphSet(ActionEvent event) {
        CreateGraphSetView gsView = new CreateGraphSetView();
        Stage gsStage = gsView.createStage();
        gsStage.show();
    }

    @FXML
    void exportAsTable(ActionEvent event) {

    }

//    @FXML
//    void filterTexts(ActionEvent event) {
//        log.debug("Filtering texts...");
//        QueryType type = this.textFilterTypeChoiceBox.getValue();
//
//        if (type == QueryType.STRING) {
//            this.filterTextsByString();
//        } else {
//            this.filterTextsByPattern();
//        }
//    }

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

        Stage createCorpusPackageStage = createCorpusPackageView.createStage();
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

            setFileOnDisk(file);
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
        this.filteredTextsListView.getItems().clear();
//        this.textsListView.getItems().clear();
//        this.textsListView.getItems().addAll(getCorpusInstance().getTexts());
    }

    @FXML
    void resetNgrams(ActionEvent event) {
        log.debug("Resetting table...");
        getQueries().clear();
    }

    @FXML
    void saveCorpusPackage(ActionEvent event) {
        log.debug("Serializing current corpus instance to a file...");

        try {
            Model.save(getCorpusInstance(), getFileOnDisk());
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
