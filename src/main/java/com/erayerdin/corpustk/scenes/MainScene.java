package com.erayerdin.corpustk.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class MainScene {
    private javafx.scene.Scene scene;
    private Parent root;
    private final String title = "Corpus Toolkit";
    private final String fxmlPath = "fxml/MainScreen.fxml";
    private final int width = 800;
    private final int height = 600;

    public MainScene() throws IOException {
        log.debug("Loading MainScene...");
        this.root = FXMLLoader.load(getClass().getClassLoader().getResource(this.fxmlPath));
        this.scene = new Scene(this.root, this.width, this.height);
    }

    public Scene getScene() {
        return this.scene;
    }

    public String getTitle() {
        return title;
    }
}
