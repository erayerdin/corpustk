package com.erayerdin.corpustk.controllers;

import com.erayerdin.corpustk.Utils;
import com.erayerdin.corpustk.core.listcells.GraphSetListCell;
import com.erayerdin.corpustk.models.Model;
import com.erayerdin.corpustk.models.graphology.GraphSet;
import com.erayerdin.corpustk.views.CreateGraphSetView;
import com.erayerdin.corpustk.views.View;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
public class ManageGraphSetsController extends Controller {

    @FXML
    private ListView<GraphSet> graphSetsListView;

    @FXML
    void addGraphSet(ActionEvent event) {
        log.debug("Adding GraphSets...");

        View createGraphSetView = new CreateGraphSetView();
        Stage stage = createGraphSetView.createStage();
        stage.showAndWait();

        this.reloadGraphSetListView();
    }

    @FXML
    void removeGraphSet(ActionEvent event) { // TODO check equals method of GraphSet
        log.debug("Removing GraphSets...");

        log.debug("Getting selections...");
        ObservableList<GraphSet> graphSetSelections = this.graphSetsListView.getSelectionModel().getSelectedItems();

        log.debug("Getting gset files...");
        File[] gsetFiles = Utils.loadGraphSetFiles();

        for (File f : gsetFiles) {
            for (GraphSet g : graphSetSelections) {
                log.debug(String.format("Creating GraphSet from file %s", f.getAbsolutePath()));

                GraphSet gObj = null;

                try {
                    gObj = (GraphSet) Model.load(f);
                } catch (IOException e) {
                    log.error("An error occured while loading gset file.", e);
                } catch (ClassNotFoundException e) {
                    log.fatal("A software error occured while loading gset file.", e);
                }

//                if (gObj.equals(g)) { // something fishy here
                if (gObj.equals(g)) {
                    log.info(String.format("Removing %s...", f.getAbsolutePath()));
                    f.delete();
                }
            }
        }

        this.reloadGraphSetListView();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("Initializing ManageGraphSetsController...");

        this.initializeGraphSetListView();
    }

    public void initializeGraphSetListView() {
        log.debug("Initializing graphset listview...");

        this.graphSetsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.graphSetsListView.getItems().addAll(Utils.loadGraphSets());
        this.graphSetsListView.setCellFactory(c -> new GraphSetListCell());
    }

    public void reloadGraphSetListView() {
        log.debug("Reloading graphset listview...");

        this.graphSetsListView.getItems().clear();
        this.graphSetsListView.getItems().addAll(Utils.loadGraphSets());
        this.graphSetsListView.refresh();
    }
}

