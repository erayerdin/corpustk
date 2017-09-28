package com.erayerdin.corpustk.controllers;

import com.erayerdin.corpustk.App;
import com.erayerdin.corpustk.models.Model;
import com.erayerdin.corpustk.models.graphology.GraphSet;
import com.erayerdin.corpustk.models.graphology.GraphSetFactory;
import com.erayerdin.linglib.graphology.GraphemeType;
import com.erayerdin.linglib.graphology.exceptions.InvalidCharSequenceLengthException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Log4j2
public class CreateGraphSetController extends Controller implements Form {

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

    @FXML
    private TextField titleTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public boolean validateForm() {
        log.debug("Validating form...");
        boolean isValid = true;

        if (this.titleTextField.getText().isEmpty()
                || this.lowerCharsTextField.getText().isEmpty()
                || this.upperCharsTextField.getText().isEmpty()) {

            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Invalid Form");
            error.setHeaderText("Form is invalid.");
            error.setContentText("GraphSet title, lower chars and upper chars shouldn't be empty.");
            error.showAndWait();
            isValid = false;
            return isValid;
        }

        return isValid;
    }

    @FXML
    void cancel(ActionEvent event) {
        this.getStage(event).close();
    }

    @FXML
    void saveGraphSet(ActionEvent event) {
        log.debug("Creating GraphSet object...");

        boolean isValid = this.validateForm();

        if (!isValid) return;

        // features
        ArrayList<GraphemeType> typesArrayList = new ArrayList<>();
        if (this.stopCheckBox.isSelected())
            typesArrayList.add(GraphemeType.STOP);

        if (this.semiStopCheckBox.isSelected())
            typesArrayList.add(GraphemeType.LINKING);

        if (this.numericCheckBox.isSelected())
            typesArrayList.add(GraphemeType.NUMERIC);

        GraphemeType[] types = typesArrayList.toArray(new GraphemeType[typesArrayList.size()]);

        GraphSet graphSet = null;
        try {
            graphSet = GraphSetFactory.createGraphSet(
                    this.lowerCharsTextField.getText(),
                    this.upperCharsTextField.getText(),
                    types
            );
            graphSet.setTitle(this.titleTextField.getText());
        } catch (InvalidCharSequenceLengthException e) {
            log.error("Lower and upper aren't equal.", e);
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Lower-Upper Length Error");
            error.setHeaderText("Lower-Upper Not Equal");
            error.setContentText("The length of lower and upper characters must be equal.");
            error.showAndWait();
            return;
        }

        try {
            log.debug("Saving GraphSet...");
            Model.save(graphSet, new File(new File(App.getUserDataDir(), "graphsets"), graphSet.getTitle().toLowerCase()+".gset")); // e.g. english.gset
            this.getStage(event).close();
        } catch (IOException e) {
            log.error("An error occured while saving GraphSet.", e);
            Alert error = new Alert(Alert.AlertType.ERROR);

            error.setTitle("GraphSet Not Saved");
            error.setHeaderText("GraphSet could not be saved.");
            error.setContentText("GraphSet could not be saved. Please make sure you have free space in your disk or your disk is not damaged.");
            error.showAndWait();
            return;
        }
    }

}
