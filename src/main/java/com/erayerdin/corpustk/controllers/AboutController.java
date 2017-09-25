package com.erayerdin.corpustk.controllers;

import com.erayerdin.corpustk.AppMeta;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import lombok.extern.log4j.Log4j2;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

@Log4j2
public class AboutController extends Controller {

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
        log.debug(String.format("Initializing %s...", this.getClass().getName()));

        this.humanReadableLabel.setText(AppMeta.getHumanReadableLabel());
        this.machineReadableLabel.setText(AppMeta.getMachineReadableLabel());
        this.groupID.setText(AppMeta.getGroupID());
        this.artifactID.setText(AppMeta.getArtifactID());
        this.version.setText(AppMeta.generateVersionString());
        this.contributorsTextArea.setText(this.generateContributorsString());
        this.licenseTextArea.setText(AppMeta.getLicense());
        this.descriptionTextArea.setText(AppMeta.getDescription());
    }

    private String generateContributorsString() {
        log.debug("Generating contributor string...");
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String[]> entry : AppMeta.getContributors().entrySet()) {
            sb.append(entry.getKey()+":\n");

            for (String c : entry.getValue()) {
                sb.append(" - "+c+"\n");
            }

            sb.append("\n");
        }

        return sb.toString();
    }
}
