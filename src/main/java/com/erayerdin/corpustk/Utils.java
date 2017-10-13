package com.erayerdin.corpustk;

import com.erayerdin.corpustk.models.Model;
import com.erayerdin.corpustk.models.graphology.GraphSet;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Log4j2
public class Utils {
    public static File[] loadGraphSetFiles() {
        log.debug("Loading GraphSet directory...");
        File dir = new File(App.getUserDataDir(), "graphsets");
        dir.mkdirs();

        log.debug("Getting .gset files from appdir...");
        File[] gsets = Arrays.stream(dir.listFiles())
                .filter(o -> o.getName().endsWith(".gset"))
                .toArray(File[]::new);

        return gsets;
    }

    public static GraphSet[] loadGraphSets() {
        log.debug("Loading GraphSets...");
        File[] gsets = Utils.loadGraphSetFiles();

        log.debug("Deserializing .gset files...");
        GraphSet[] graphSets1 = new GraphSet[gsets.length]; // filtered for null values later

        int i = 0;
        for (File gset : gsets) {
            log.debug(String.format("Loading %s...", gset.getName()));
            GraphSet graphSet = null;

            try {
                graphSet = (GraphSet) Model.load(gset.getAbsoluteFile());
            } catch (IOException e) {
                log.error("Could not read gset file...", e);
            } catch (ClassNotFoundException e) {
                log.debug("Could not find the corresponding class...", e);
            }

            graphSets1[i] = graphSet;
            i++;
        }

        GraphSet[] graphSets2 = Arrays.stream(graphSets1)
                .filter(o -> o != null)
                .toArray(GraphSet[]::new);

        return graphSets2;
    }

    public static void generateErrorAlert(String title, String header, String content) {
        log.debug("Generating error alert...");

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);

        Label contentLabel = new Label(content);
        contentLabel.setMaxWidth(300);
        contentLabel.setWrapText(true);
        alert.getDialogPane().setContent(contentLabel);

        alert.showAndWait();
    }

    public static String readTextFile(File path) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(path.getAbsolutePath()));
        } catch (IOException e) {
            log.error("An error occured while reading text file...", e);
            return null;
        }
        StringBuilder sb = new StringBuilder();

        lines.stream().forEach(line -> {
            sb.append(line+"\n");
        });

        return sb.toString();
    }
}
