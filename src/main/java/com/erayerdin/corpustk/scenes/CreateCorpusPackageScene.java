package com.erayerdin.corpustk.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class CreateCorpusPackageScene {
    private Scene scene;
    private Parent root;
    private final String title = "Corpus Toolkit - Create Corpus Package";
    private final String fxmlPath = "fxml/CreateCorpusPackageScreen.fxml";

    public CreateCorpusPackageScene() {
        log.debug(String.format("Loading %s...", this.getClass().getName()));
        try {
            this.root = FXMLLoader.load(getClass().getClassLoader().getResource(this.fxmlPath));
        } catch (IOException e) {
            log.error(String.format("An error occured while loading %s...", this.getClass().getName()), e);
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
