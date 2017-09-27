package com.erayerdin.corpustk;

import com.erayerdin.corpustk.models.graphology.GraphSet;
import com.erayerdin.corpustk.views.MainView;
import com.erayerdin.corpustk.views.SplashView;
import com.erayerdin.corpustk.views.View;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;

import java.io.IOException;

@Log4j2
public class App extends Application {
    @Getter @Setter private static Stage focalStage;
    @Getter private static String userDataDir = AppDirsFactory.getInstance().getUserDataDir(
            AppMeta.getMachineReadableLabel(),
            "1",
            (String) AppMeta.getContributors().keySet().toArray()[0]
    );

    public App() {
        super();
    }

    private void loadGraphSetsAvailable() {
        log.debug("Loading available GraphSets...");
        // TODO load graphsets here
    }

    private void preload() {
        log.debug("Preloading resources...");
        // TODO preloading section
    }

    public static void main( String[] args ) {
        log.debug("Running application...");
        Application.launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        this.preload();

        focalStage = primaryStage;
        focalStage.getIcons().add(new Image("img/icon-128.png"));
        focalStage.setTitle("Loading...");

        SplashView splashView = new SplashView();
        if (splashView.isDecorated() == false)
            focalStage.initStyle(StageStyle.UNDECORATED);

        Scene splashScene = splashView.createScene();
        focalStage.setScene(splashScene);

        focalStage.show();

        // https://stackoverflow.com/a/29444159

        log.debug("Pausing 5 seconds for SplashScreen...");
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e -> {
            focalStage.close();

            MainView mainView = new MainView();
            Scene mainScene = null;
            try {
                mainScene = mainView.createScene();
            } catch (IOException e1) {
                log.error(String.format("An error occured while loading %s.", mainView.toString()), e1);
                System.exit(1);
            }
            focalStage = new Stage();
            focalStage.setScene(mainScene);
            focalStage.setTitle(mainView.getTitle());
            focalStage.show();
        });
        pause.play();
    }

    public void stop() throws Exception {
        log.debug("Exiting application...");
    }

    // TODO write a graphSet loader
}
