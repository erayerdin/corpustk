package com.erayerdin.corpustk.controllers;

import com.erayerdin.corpustk.Utils;
import com.erayerdin.corpustk.core.listcells.TextListCell;
import com.erayerdin.corpustk.models.Model;
import com.erayerdin.corpustk.models.corpus.Corpus;
import com.erayerdin.corpustk.models.corpus.GramType;
import com.erayerdin.corpustk.models.corpus.QueryResult;
import com.erayerdin.corpustk.models.corpus.Text;
import com.erayerdin.corpustk.views.*;
import com.erayerdin.linglib.corpus.QueryType;
import com.erayerdin.linglib.corpus.Token;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Log4j2
public class MainController extends Controller {

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem newCorpusPackageMenuItem;

    @FXML
    private MenuItem openCorpusPackageMenuItem;

    @FXML
    private MenuItem saveCorpusPackageMenuItem;

    @FXML
    private MenuItem saveCorpusPackageAsMenuItem;

    @FXML
    public MenuItem currentGraphSetMenuItem;

    @FXML
    private MenuItem quitMenuItem;

    @FXML
    private Button newCorpusPackageButton;

    @FXML
    private Button openCorpusPackageButton;

    @FXML
    private Button saveCorpusPackageButton;

    @FXML
    private Button importTextButton;

    @FXML
    private TextField textFilterTextField;

    @FXML
    private ChoiceBox<QueryType> textFilterTypeChoiceBox;

    @FXML
    private Button exportAsTable;

    @FXML
    private TextField ngramQueryTextField;

    @FXML
    private TextField ngramDepthTextField;

    @FXML
    private ChoiceBox<QueryType> ngramTypeChoiceBox;

    @FXML
    private ChoiceBox<GramType> ngramQueryTypeChoiceBox;

    @FXML
    private Button ngramSearchButton;

    @FXML
    private Button resetNgramButton;

    @FXML
    private TableView<QueryResult> ngramsTableView;

    @FXML
    private TableColumn<QueryResult, String> queryTableQueryColumn;

    @FXML
    private TableColumn<QueryResult, String> queryTableResultColumn;

    @FXML
    private TableColumn<QueryResult, String> queryTableTotalColumn;

    @FXML
    private ListView<Text> textsListView;

    @FXML
    void about(ActionEvent event) {
        View aboutView = new AboutView();
        Stage stage = aboutView.createStage();
        stage.show();
    }

    @FXML
    void currentGraphSet(ActionEvent event) {
        View currentGraphSetView = new CurrentGraphSetView();
        Stage stage = currentGraphSetView.createStage();
        stage.show();
    }

    @FXML
    void exportAsTable(ActionEvent event) {

    }

    @FXML
    void importText(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("Text File (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(ext);

        List<File> files = fileChooser.showOpenMultipleDialog(this.getWindow(event));

        files.stream().forEach(file -> {
            String text = Utils.readTextFile(file);

            if (text != null) {
                Text obj = new Text(text, corpusInstance.get().getGraphSet());
                log.debug(String.format("Adding %s text object to corpus instance...", obj.toString()));
                corpusInstance.get().getTexts().add(obj);
            }
        });
    }

    @FXML
    void manageGraphSets(ActionEvent event) {
        View manageGraphSetsView = new ManageGraphSetsView();
        Stage stage = manageGraphSetsView.createStage();
        stage.show();
    }

    @FXML
    void newCorpusPackage(ActionEvent event) {
        View createCorpusPackageView = new CreateCorpusPackageView();
        Stage stage = createCorpusPackageView.createStage();
        stage.show();
    }

    @FXML
    void openCorpusPackage(ActionEvent event) {
        log.debug("Showing OpenCorpusPackage dialog...");

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("Corpus File (*.crp)", "*.crp");
        fileChooser.getExtensionFilters().add(ext);
        File file = fileChooser.showOpenDialog(this.importTextButton.getScene().getWindow()); // patched, menuitem cannot be cast to source

        if (file != null) {
            log.debug("Deserializing corpus package...");
            Model corpus;

            try {
                corpus = Model.load(file);
            } catch (IOException e) {
                log.error("Corpus package could not be read.", e);
                Utils.generateErrorAlert(
                        "Could Not Read Corpus Package",
                        "Corpus Package could not be read.",
                        "Please make sure you selected a correct corpus package file or your file system is not damaged."
                );
                return;
            } catch (ClassNotFoundException e) {
                log.fatal("Could not find Corpus class.", e);
                return;
            }

            corpusInstance.set((Corpus) corpus);
            fileOnDisk = file;
        } else {
            log.warn("Corpus package is not selected. Returning...");
            return;
        }

        this.textsListView.setItems(textFilteredList);
    }

    @FXML
    void quit(ActionEvent event) {
        this.getStage(event).close();
    }

    @FXML
    void resetNgrams(ActionEvent event) {
        log.debug("Resetting ngram table...");
        this.ngramsTableView.getItems().clear();
    }

    @FXML
    void saveCorpusPackage(ActionEvent event) {
        log.debug("Saving corpus package...");

        try {
            Model.save(corpusInstance.get(), fileOnDisk);
        } catch (IOException e) {
            log.error("An error occured while saving corpus package...", e);
            Utils.generateErrorAlert(
                    "Could not Save Corpus Package",
                    "Corpus Package could not be saved.",
                    "Please make sure your file system is not damaged or full."
            );
            return;
        }
    }

    @FXML
    void saveCorpusPackageAs(ActionEvent event) {
        log.debug("Save As for corpus fired.");

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("Corpus Package (*.crp)", "*.crp");
        fileChooser.getExtensionFilters().add(ext);
        File file = fileChooser.showSaveDialog(this.importTextButton.getScene().getWindow()); // patched, menuitem cannot be cast to source

        if (file != null) {
            try {
                Model.save(corpusInstance.get(), file);
            } catch (IOException e) {
                log.error("An error occured while saving corpus package.", e);

                Utils.generateErrorAlert(
                        "Could not Save Corpus Package",
                        "Corpus Package could not be saved.",
                        "Please make sure your file system is not damaged or full."
                );

                return;
            }
        } else {
            log.warn("File to save not selected. Returning...");
        }
    }

    @FXML
    void searchNgrams(ActionEvent event) {
        String query = this.ngramQueryTextField.getText();
        int depth = Integer.parseInt(this.ngramDepthTextField.getText());
        GramType gType = this.ngramQueryTypeChoiceBox.getValue();
        QueryType qType = this.ngramTypeChoiceBox.getValue();

        log.debug(String.format("Making queries -- GramType: %s | QueryType: %s | Depth: %d", gType.toString(), qType.toString(), depth));

        this.ngramsTableView.getItems().clear();

        List<QueryResult> results = new ArrayList<>();

        switch (gType) {
            case PREGRAM:
                results = Text.pregrams(this.textFilteredList, getCorpusInstance().getGraphSet(), depth, query);
                break;
            case POSTGRAM:
                results = Text.postgrams(this.textFilteredList, getCorpusInstance().getGraphSet(), depth, query);
                break;
        }

        this.ngramsTableView.getItems().addAll(results);
        this.ngramsTableView.refresh();
    }

    private static ObjectProperty<Corpus> corpusInstance = new SimpleObjectProperty<>(null);
    @Getter @Setter private static File fileOnDisk;
    @Getter private FilteredList<Text> textFilteredList = new FilteredList<Text>(FXCollections.observableArrayList(), data -> true);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("Initializing MainController...");

        // Listeners
        this.enableElementsListener();
        this.initializeFilteredList();
        this.initializeTexts();
        this.initializeHotkeys();
        this.initializeChoiceBoxes();
        this.initializeQueryTableView();

        // Ngram Depth Initializer
        this.ngramDepthFieldListener();


        // Close handler
        // TODO implement close request later
//        log.debug("Adding close handler...");
//        this.resetNgramButton.getScene().getWindow().setOnCloseRequest(e -> {
//            log.debug("Closing MainScreen...");
//
//            // TODO implement alert here later
//        });
    }

    /**
     * Listens corpus and enables/disables corresponding elements.
     */
    private void enableElementsListener() {
        log.debug("Adding corpus-related element listener to corpus instance...");

        corpusInstance.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                log.debug("Enabling corpus-related elements...");
                this.enableElements(true);
            } else {
                log.debug("Disabling corpus-related elements...");
                this.enableElements(false);
            }
        });
    }

    /**
     * Set corpus-related elements as enabled or disabled.
     *
     * @param enabled
     */
    private void enableElements(boolean enabled) {
        if (enabled)
            log.debug("Enabling corpus-related elements...");
        else
            log.debug("Disabling corpus-related elements...");

        boolean disabled = !enabled;

        this.saveCorpusPackageMenuItem.setDisable(disabled);
        this.saveCorpusPackageAsMenuItem.setDisable(disabled);
        this.currentGraphSetMenuItem.setDisable(disabled);

        this.saveCorpusPackageButton.setDisable(disabled);
        this.importTextButton.setDisable(disabled);
        this.textFilterTextField.setDisable(disabled);
        this.textFilterTypeChoiceBox.setDisable(disabled);
        this.exportAsTable.setDisable(disabled);

        this.ngramQueryTextField.setDisable(disabled);
        this.ngramTypeChoiceBox.setDisable(disabled);
        this.ngramDepthTextField.setDisable(disabled);
        this.ngramQueryTypeChoiceBox.setDisable(disabled);
        this.ngramSearchButton.setDisable(disabled);
        this.resetNgramButton.setDisable(disabled);
        this.ngramTypeChoiceBox.setDisable(disabled);
        this.ngramsTableView.setDisable(disabled);
        this.textsListView.setDisable(disabled);
    }

    /**
     * Initializes text filtered list based on corpus...
     */
    private void initializeFilteredList() {
        log.debug("Initializing filtered text list by adding listener...");

        corpusInstance.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                log.debug("Constructing text filtered list...");
                this.textFilteredList = new FilteredList<Text>(newValue.getTexts());
            } else {
                log.debug("Deconstructing text filtered list...");
                this.textFilteredList = new FilteredList<Text>(null);
            }
        });

        log.debug("Adding listener to filter field for text filtering...");
        this.textFilterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            textFilteredList.setPredicate(text -> {
                if (newValue.length() < 2) {
                    log.debug("Tag field is less than 2 characters. More characters needed.");
                    return true;
                }

                log.debug("Filtering texts...");
                return text.getTags().stream().anyMatch(t -> t.contains(newValue));
            });
        });
    }

    /**
     * Initializes ChoiceBox elements with enums.
     */
    private void initializeChoiceBoxes() {
        log.debug("Initializing Text filter types to ChoiceBox element...");
        this.textFilterTypeChoiceBox.getItems().addAll(QueryType.values());
        this.textFilterTypeChoiceBox.getSelectionModel().selectFirst();

        log.debug("Initializing n-gram filter types to ChoiceBox element...");
        this.ngramTypeChoiceBox.getItems().addAll(QueryType.values());
        this.ngramTypeChoiceBox.getSelectionModel().selectFirst();

        log.debug("Initializing n-gram position types to ChoiceBox element...");
        this.ngramQueryTypeChoiceBox.getItems().addAll(GramType.values());
        this.ngramQueryTypeChoiceBox.getSelectionModel().selectFirst();
    }

    /**
     * Initializes text listview...
     */
    private void initializeTexts() {
        log.debug("Initialize texts...");
        this.textsListView.setItems(textFilteredList);

        log.debug("Setting cell factory of text listview...");
        this.textsListView.setCellFactory(c -> new TextListCell());

        log.debug("Adding double-click listener to text listview...");
        this.textsListView.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2) {
                openText();
            }
        });
    }

    /**
     * Initializing hotkeys...
     */
    private void initializeHotkeys() {
        // https://blog.idrsolutions.com/2014/04/tutorial-how-to-setup-key-combinations-in-javafx/
        log.debug("Initializing hotkeys for menu items...");

        this.newCorpusPackageMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        this.openCorpusPackageMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        this.saveCorpusPackageMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
    }

    /**
     * Initializes table view for queries.
     */
    private void initializeQueryTableView() {
        log.debug("Initializing query table view...");

        this.queryTableQueryColumn.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getQuery()));
        this.queryTableResultColumn.setCellValueFactory(value -> {
            StringBuilder sb = new StringBuilder();
            sb.append("[");

            Token[] tokens = value.getValue().getTokens();

            for (int i = 0 ; i < tokens.length ; i++) {
                Token t = tokens[i];

                if (t == null) {
                    continue;
                }

                if (i == tokens.length-1) {
                    sb.append(t.lowerize()+"]");
                } else {
                    sb.append(t.lowerize()+", ");
                }
            }

            String repr = sb.toString();

            if (repr.equals("["))
                repr = "";

            return new SimpleStringProperty(repr);
        });
        this.queryTableTotalColumn.setCellValueFactory(value -> new SimpleStringProperty(Integer.toString(value.getValue().getSize())));
    }

    /**
     * Initializes ngram depth field.
     */
    private void ngramDepthFieldListener() {
        this.ngramDepthTextField.setText("1");

        log.debug("Adding ngram depth field listener...");
        this.ngramDepthTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            Integer val = null;
            try {
                val = Integer.parseInt(newValue);
                log.debug("Ngram depth is valid.");
            } catch (NumberFormatException e) {
                log.warn("Ngram depth is invalid. Setting to 1.");
                this.ngramDepthTextField.setText("1");
            }
        });
    }

    /**
     * Opening text view for text...
     */
    private void openText() {
        Text obj = this.textsListView.getSelectionModel().getSelectedItem();
        log.debug(String.format("Opening %s text...", obj.toString()));

        TextController.setTextInstance(obj);

        View textView = new TextView();
        Stage stage = textView.createStage();
        stage.showAndWait();

        textsListView.refresh();
        // TODO tooltip refreshing didn't work, see here later
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
}

