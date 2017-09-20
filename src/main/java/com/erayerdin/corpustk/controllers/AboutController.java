package com.erayerdin.corpustk.controllers;

import com.erayerdin.corpustk.AppMeta;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import lombok.extern.log4j.Log4j2;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

@Log4j2
public class AboutController implements Initializable {

    @FXML
    private Label humanReadableLabel;

    @FXML
    private Label machineReadableLabel;

    @FXML
    private Label groupID;

    @FXML
    private Label artifactID;

    @FXML
    private Label version;

    @FXML
    private TextArea contributorsTextArea;

    @FXML
    private TextArea licenseTextArea;

    @FXML
    private TextArea descriptionTextArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("Initializing AboutController components...");
        this.humanReadableLabel.setText(AppMeta.getHumanReadableLabel());
        this.machineReadableLabel.setText(AppMeta.getMachineReadableLabel());
        this.groupID.setText(AppMeta.getGroupID());
        this.artifactID.setText(AppMeta.getArtifactID());
        this.version.setText(AppMeta.generateVersionString());
        this.contributorsTextArea.setText(AboutController.generateContibutorsString());
        this.licenseTextArea.setText(AppMeta.getLicense());
        this.descriptionTextArea.setText(AppMeta.getDescription());
    }

    private static String generateContibutorsString() {
        log.debug("Getting contributors and creating StringBuilder...");
        HashMap<String, String[]> contributors = AppMeta.getContributors();
        StringBuilder mainBuilder = new StringBuilder();

        contributors.forEach((k,v) -> {
            log.debug("Processing contributor: "+k);
            StringBuilder contBuilder = new StringBuilder();
            contBuilder.append(k+":\n");

            for (String c : v) {
                log.debug("Processing contribution: "+c);
                contBuilder.append(" - "+c+"\n");
            }
            contBuilder.append("\n");

            log.debug("Appending to main StringBuilder");
            mainBuilder.append(contBuilder.toString());
        });

        return mainBuilder.toString().trim();
    }
}
