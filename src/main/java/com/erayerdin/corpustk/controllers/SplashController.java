package com.erayerdin.corpustk.controllers;

import com.erayerdin.corpustk.AppMeta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Window;
import lombok.extern.log4j.Log4j2;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
public class SplashController extends Controller {

    @FXML
    private Text metaText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug(String.format("Initializing %s...", this.getClass().getName()));
        this.metaText.setText(AppMeta.generateVersionString() + " by Eray Erdin in 2017 licensed under the terms of Apache Software License 2.0");
    }

    @Override
    public Scene getScene(ActionEvent event) {
        Node source = (Node) event.getSource();
        Scene scene = source.getScene();

        return scene;
    }

    @Override
    public Window getWindow(ActionEvent event) {
        return this.getScene(event).getWindow();
    }
}
