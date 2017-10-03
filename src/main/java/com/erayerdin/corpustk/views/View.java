package com.erayerdin.corpustk.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
@ToString(exclude = {"baseDir", "title", "width", "height", "decorated", "resizable", "modal"})
public abstract class View {
    private static final String baseDir = "fxml";
    @Getter private static final Image icon = new Image("img/icon-128.png");

    private String viewName;
    private StringProperty title;
    private Integer width = null;
    private Integer height = null;
    @Getter @Setter private boolean decorated = true;
    @Getter @Setter private boolean resizable = true;
    @Getter @Setter private boolean modal = false;

    public View() {
        log.debug(String.format("Creating %d...", this.hashCode()));
        this.title = new SimpleStringProperty(null);
    }

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

    public Stage createStage() {
        log.debug(String.format("Creating stage for %s...", this.toString()));
        Stage stage = new Stage();

        if (this.getTitle() != null)
            stage.setTitle(this.getTitle());

        try {
            stage.setScene(this.createScene());
        } catch (IOException e) {
            log.error("An error occured while creating scene...", e);
            System.exit(1);
        }

        stage.setResizable(this.isResizable());

        if (!this.isDecorated()) {
            stage.initStyle(StageStyle.UNDECORATED);
        }

        if (this.isModal())
            stage.initModality(Modality.APPLICATION_MODAL);

        stage.getIcons().add(getIcon());
        return stage;
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
