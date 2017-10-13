package com.erayerdin.corpustk.controllers;

import com.erayerdin.corpustk.Utils;
import com.erayerdin.corpustk.core.listcells.GraphSetListCell;
import com.erayerdin.corpustk.models.graphology.GraphSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import lombok.extern.log4j.Log4j2;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
public class ManageGraphSetsController extends Controller {

    @FXML
    private ListView<GraphSet> graphSetsListView;

    @FXML
    void addGraphSet(ActionEvent event) {

    }

    @FXML
    void removeGraphSet(ActionEvent event) {

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

