package com.erayerdin.corpustk.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import lombok.extern.log4j.Log4j2;

import java.net.URL;
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
    public void validateForm() {

    }

    @FXML
    void cancel(ActionEvent event) {

    }

    @FXML
    void saveGraphSet(ActionEvent event) {

    }

}
