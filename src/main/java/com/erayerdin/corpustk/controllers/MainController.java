package com.erayerdin.corpustk.controllers;

import com.erayerdin.corpustk.views.AboutView;
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
    private ListView<?> textsListView;

    //////////////////
    // Model Fields //
    //////////////////

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug(String.format("Initializing %s...", this.getClass().getName()));
    }

    ///////////////////
    // Model Methods //
    ///////////////////

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

    }

    @FXML
    void openCorpusPackage(ActionEvent event) {

    }

    @FXML
    void quit(ActionEvent event) {

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
