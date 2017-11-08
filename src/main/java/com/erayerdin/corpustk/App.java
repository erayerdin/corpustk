package com.erayerdin.corpustk;

import com.erayerdin.corpustk.views.MainView;
import com.erayerdin.corpustk.views.SplashView;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.File;

@Log4j2
public class App extends Application {
    @Getter @Setter private static Stage focalStage;
//    @Getter private static String userDataDir = AppDirsFactory.getInstance().getUserDataDir(
//            AppMeta.getMachineReadableLabel(),
//            "1",
//            (String) AppMeta.getContributors().keySet().toArray()[0]
//    );
    @Getter private static String userDataDir;

    public App() {
        super();
        this.preload();
    }

    private void loadGraphSetsAvailable() {
        log.debug("Loading available GraphSets...");
        // TODO load graphsets here
    }

    private void preload() {
        log.debug("Preloading resources...");
        this.initializeUserDataDir();
    }

    private void initializeUserDataDir() {
        log.debug("Initializing user data directory.");

        String OS = System.getProperty("os.name");
        String appDir = null;

        if (OS.contains("Windows")) {
            log.debug("Setting user data directory for Windows...");

            appDir = System.getenv("AppData");
        } else if (OS.equals("Linux")) {
            log.debug("Setting user data directory for nix-based operating systems...");

            appDir = System.getProperty("user.home");
            appDir += "/.local/share/corpustk";
        } else {
            log.warn(String.format("%s as operating system is not supported.", OS));
            Platform.exit();
        }

        App.userDataDir = appDir;

        File appDirObj = new File(App.userDataDir);

        if (!appDirObj.exists()) {
            log.debug("Creating user data directories...");

            appDirObj.mkdirs();
            (new File(appDirObj, "graphsets")).mkdirs();
        }
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

        focalStage = splashView.createStage();
        focalStage.show();

        // https://stackoverflow.com/a/29444159

        log.debug("Pausing 5 seconds for SplashScreen...");
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e -> {
            focalStage.close();

            MainView mainView = new MainView();
            Stage mainStage = mainView.createStage();

            focalStage = mainStage;
            focalStage.setMaximized(true);
            focalStage.show();
        });
        pause.play();
    }

    public void stop() throws Exception {
        log.debug("Exiting application...");
    }

    // TODO write a graphSet loader
}
