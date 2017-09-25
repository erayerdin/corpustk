package com.erayerdin.corpustk.views;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
@ToString(exclude = {"baseDir", "title", "width", "height", "decorated"})
public abstract class View {
    private static final String baseDir = "fxml";

    private String viewName;
    private StringProperty title;
    private Integer width = null;
    private Integer height = null;
    private boolean decorated = true;

    public View() {log.debug(String.format("Creating %d...", this.hashCode()));}

    public View(String viewName, String title) {
        this.viewName = viewName;
        this.title = new SimpleStringProperty(title);
        log.debug(String.format("Created %s.", this.toString()));
    }

    public View(String viewName, String title, Integer width, Integer height) {
        this.viewName = viewName;
        this.title = new SimpleStringProperty(title);
        this.width = width;
        this.height = height;
        log.debug(String.format("Created %s.", this.toString()));
    }

    public Scene createScene() throws IOException {
        log.debug(String.format("Creating scene for %s...", this.toString()));

        log.debug("Loading FXML file...");
        String viewPath = this.getViewPath();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(viewPath));

        log.debug("Creating scene...");
        Scene scene = null;
        if (this.getWidth() == null || this.getHeight() == null)
            scene = new Scene(root);
        else
            scene = new Scene(root, this.getWidth().intValue(), this.getHeight().intValue());

        return scene;
    }

    public String getViewPath() {
        return baseDir+"/"+this.getViewName()+".fxml";
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isDecorated() {
        return this.decorated;
    }

    public void setDecorated(boolean decorated) {
        this.decorated = decorated;
    }
}
