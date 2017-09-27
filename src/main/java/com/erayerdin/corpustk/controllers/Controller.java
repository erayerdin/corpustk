package com.erayerdin.corpustk.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public abstract class Controller implements Initializable {
    public Scene getScene(ActionEvent event) {
        Node source = (Node) event.getSource();
        Scene scene = source.getScene();

        return scene;
    }

    public Window getWindow(ActionEvent event) {
        return this.getScene(event).getWindow();
    }

    public Stage getStage(ActionEvent event) {
        return (Stage) this.getWindow(event);
    }
}
