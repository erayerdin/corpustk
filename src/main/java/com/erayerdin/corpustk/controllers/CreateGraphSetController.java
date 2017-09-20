package com.erayerdin.corpustk.controllers;

import com.erayerdin.corpustk.core.graphology.GraphSetInstance;
import com.erayerdin.linglib.graphology.GraphSet;
import com.erayerdin.linglib.graphology.GraphSetFactory;
import com.erayerdin.linglib.graphology.GraphemeType;
import com.erayerdin.linglib.graphology.exceptions.InvalidCharSequenceLengthException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
public class CreateGraphSetController implements Initializable {

    @FXML
    private TextField titleTextField;

    @FXML
    private CheckBox numericCheckBox;

    @FXML
    private CheckBox semiStopCheckBox;

    @FXML
    private CheckBox stopCheckBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField lowerCharsTextField;

    @FXML
    private TextField upperCharsTextField;

    private GraphSetInstance graphSetInstance = null;

    private void closeCurrentWindow(ActionEvent event) {
        // https://stackoverflow.com/a/11476162/2926992
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void cancel(ActionEvent event) {
        this.closeCurrentWindow(event);
    }

    @FXML
    void saveGraphSet(ActionEvent event) {
        log.debug("Counting features...");
        int featureCount = 0;

        if (numericCheckBox.isSelected()) featureCount++;
        if (semiStopCheckBox.isSelected()) featureCount++;
        if (stopCheckBox.isSelected()) featureCount++;

        log.debug("Defining types...");
        GraphemeType[] types = new GraphemeType[featureCount];

        int typeIndex = 0;

        if (numericCheckBox.isSelected()) {
            types[typeIndex] = GraphemeType.NUMERIC;
            typeIndex++;
        }

        if (semiStopCheckBox.isSelected()) {
            types[typeIndex] = GraphemeType.LINKING;
            typeIndex++;
        }

        if (stopCheckBox.isSelected()) {
            types[typeIndex] = GraphemeType.STOP;
        }

        log.debug("Validating title...");
        if (this.titleTextField.getText() == "" || this.titleTextField.getText() == null) {
            log.error("Title is empty.");
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Title Empty");
            error.setHeaderText("Title field is empty.");
            error.setContentText("Title field must have a value.");
            error.showAndWait();
            return;
        }

        log.debug("Creating GraphSet...");
        GraphSet graphSet = null;
        try {
            graphSet = GraphSetFactory.createGraphSet(
                    this.lowerCharsTextField.getText(),
                    this.upperCharsTextField.getText(),
                    types
            );
        } catch (InvalidCharSequenceLengthException e) {
            log.error("An error occured while creating GraphSet...");
            log.error(e.getMessage());
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle(e.getClass().getName());
            error.setHeaderText("Upper and Lower Char Length Invalid");
            error.setContentText("Upper and lower character sections must have the same length!");
            error.showAndWait();
            return;
        }

        log.debug("Setting GraphSetInstance...");
        this.graphSetInstance = new GraphSetInstance(this.titleTextField.getText(), graphSet);

        log.debug("Saving GraphSetInstance to userDir...");
        try {
            this.graphSetInstance.save();
        } catch (IOException e) {
            log.error("An error occured while saving GraphSetInstance to userDir...", e);
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("GraphSet Not Saved");
            error.setHeaderText("GraphSet could not be saved into disk.");
            error.setContentText("Please make sure your disk is not damaged or full.");
            error.showAndWait();
            this.closeCurrentWindow(event);
        }
        this.closeCurrentWindow(event);
    }

    public GraphSetInstance getGraphSetInstance() {
        return graphSetInstance;
    }
}
