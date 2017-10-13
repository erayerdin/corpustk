package com.erayerdin.corpustk.controllers;

import com.erayerdin.corpustk.Utils;
import com.erayerdin.corpustk.models.graphology.GraphSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import lombok.extern.log4j.Log4j2;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

@Log4j2
public class CurrentGraphSetController extends Controller {

    @FXML
    private TextArea graphSetInfoTextArea;

    @FXML
    private Button saveGraphSetButton;

    @FXML
    void saveGraphSet(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("Initializing CurrentGraphSetController...");

        this.checkAvailability();
        this.graphSetInfoTextArea.setText(this.buildGraphSetInfoText(MainController.getCorpusInstance().getGraphSet()));
    }

    private void checkAvailability() {
        log.debug("Checking availability of current GraphSet...");

        boolean isAvailable = false;
        GraphSet[] gsets = Utils.loadGraphSets();

        for (GraphSet g : gsets) {
            if (g.equals(MainController.getCorpusInstance().getGraphSet())) {
                log.info("Current GraphSet is available.");
                isAvailable = true;
                break;
            }
        }

        if (isAvailable) {
            log.debug("Disabling Save button...");
            this.saveGraphSetButton.setDisable(true);
        }
    }

    private String buildGraphSetInfoText(GraphSet gset) {
        log.debug("Building GraphSet string...");
        StringBuilder sb = new StringBuilder();
        sb.append(gset.getTitle()+"\n\n");

        Arrays.stream(gset.getGraphemes()).forEach(g -> {
            sb.append(g.getLower());
        });
        sb.append("\n");

        Arrays.stream(gset.getGraphemes()).forEach(g -> {
            sb.append(g.getUpper());
        });

        return sb.toString();
    }
}

