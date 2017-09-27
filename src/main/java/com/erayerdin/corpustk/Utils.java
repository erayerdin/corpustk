package com.erayerdin.corpustk;

import com.erayerdin.corpustk.models.Model;
import com.erayerdin.corpustk.models.graphology.GraphSet;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Log4j2
public class Utils {
    public static GraphSet[] loadGraphSets() {
        log.debug("Loading GraphSets...");
        File dir = new File(App.getUserDataDir(), "graphsets");
        dir.mkdirs();

        log.debug("Getting .gset files from appdir...");
        File[] gsets = Arrays.stream(dir.listFiles())
                .filter(o -> o.getName().endsWith(".gset"))
                .toArray(File[]::new);

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
}
