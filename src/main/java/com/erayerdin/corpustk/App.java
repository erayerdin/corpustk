package com.erayerdin.corpustk;

import com.erayerdin.corpustk.core.corpus.Corpus;
import com.erayerdin.corpustk.core.corpus.TextInstance;
import com.erayerdin.corpustk.core.graphology.GraphSetInstance;
import com.erayerdin.corpustk.scenes.MainScene;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

@Log4j2
public class App extends Application {
    private static Stage stage;
    private static AppDirs appDirs = AppDirsFactory.getInstance();
    private static String userDataString = appDirs.getUserDataDir(AppMeta.getMachineReadableLabel(), "1", "erayerdin");
    @Getter private static File userDataDir = new File(userDataString);
    @Getter private static File graphsetDir = new File(userDataString, "graphsets");

    @Getter private static GraphSetInstance[] graphSetInstances;
    @Getter private static GraphSetInstance currentGraphSetInstance;

    @Getter @Setter private static SimpleObjectProperty<Corpus> corpusObj = new SimpleObjectProperty<>(null);
    @Getter @Setter private static ObservableList<TextInstance> textInstances;
    @Getter @Setter private static File corpusFile;

    public App() {
        super();
        // Create User Directory
        log.debug("Creating user directory...");
        App.userDataDir.mkdirs();
        App.graphsetDir.mkdirs();
    }

    public static void main( String[] args ) {
        log.debug("Running application...");
        Application.launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        this.preload();

        setStage(primaryStage);
        log.debug("Loading FXML file and setting up new scene...");
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/SplashScreen.fxml"));
        Scene scene = new Scene(root, 1920/2, 1080/2);

        log.debug("Decorating splash screen...");
        this.getStage().setResizable(false);
        this.getStage().initStyle(StageStyle.UNDECORATED);

        log.debug("Setting scene and showing...");
        this.getStage().setScene(scene);
        this.getStage().show();

        // https://stackoverflow.com/a/29444159
        log.debug("Pausing 5 seconds...");
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e -> {
            log.debug("Setting main screen...");

            log.debug("Closing current stage...");
            App.getStage().close();
            log.debug("Initializing new stage...");
            Stage stage = new Stage();
            log.debug("Injecting new stage to App...");
            App.setStage(stage);

            MainScene mainScene = null;
            try {
                mainScene = new MainScene();
            } catch (IOException e1) {
                log.error("An error occured while loading main scene...");
                log.error(e1.getMessage());
                System.exit(1);
            }

//            App.getStage().initStyle(StageStyle.DECORATED);
            App.getStage().setScene(mainScene.getScene());
            App.getStage().setTitle(mainScene.getTitle());
            App.getStage().show();
        });
        pause.play();
    }

    public void stop() throws Exception {
        log.debug("Exiting application...");
    }

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        App.stage = stage;
    }

    public static Corpus getCorpusObj() {
        return corpusObj.get();
    }

    public static SimpleObjectProperty<Corpus> corpusObjProperty() {
        return corpusObj;
    }

    public static void setCorpusObj(Corpus corpusObj) {
        App.corpusObj.set(corpusObj);
    }

    private void preload() throws IOException, ClassNotFoundException {
        log.debug("##############");
        log.debug("# Preloading #");
        log.debug("##############");
        // Loading GraphSetInstances
        this.loadGraphSetInstances();
    }

    private void loadGraphSetInstances() throws IOException, ClassNotFoundException {
        log.debug("Loading GraphSetInstances...");

        File[] gsetFiles = getGraphsetDir().listFiles();
        GraphSetInstance[] gsetInstances = new GraphSetInstance[gsetFiles.length];

        for (int i=0 ; i < gsetFiles.length ; i++) {
            File f = gsetFiles[i];
            log.debug(String.format("Processing gset file: %s", f.getAbsolutePath()));
            GraphSetInstance gsetInstance = GraphSetInstance.load(f);
            gsetInstances[i] = gsetInstance;
        }

        App.graphSetInstances = gsetInstances;
    }
}
