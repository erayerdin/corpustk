package com.erayerdin.corpustk.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class AboutScene {
    private javafx.scene.Scene scene;
    private Parent root;
    private final String title = "About Corpus Toolkit";
    private final String fxmlPath = "fxml/AboutScreen.fxml";

    public AboutScene() {
        log.debug("Loading AboutScene...");
        try {
            this.root = FXMLLoader.load(getClass().getClassLoader().getResource(this.fxmlPath));
        } catch (IOException e) {
            log.error("An error occured while loading AboutScene...");
            log.error(e.getMessage());
            System.exit(1);
        }
        this.scene = new Scene(this.root);
    }

    public Scene getScene() {
        return this.scene;
    }

    public String getTitle() {
        return title;
    }
}
