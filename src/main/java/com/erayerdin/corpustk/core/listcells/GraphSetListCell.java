package com.erayerdin.corpustk.core.listcells;

import com.erayerdin.corpustk.models.graphology.GraphSet;
import com.erayerdin.linglib.graphology.Grapheme;
import com.erayerdin.linglib.graphology.GraphemeType;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;

// https://www.billmann.de/2013/07/03/javafx-custom-listcell/
@Log4j2
public class GraphSetListCell extends ListCell<GraphSet> {

    public void updateItem(GraphSet gset, boolean empty) {
        super.updateItem(gset, empty);

        log.debug("Building GraphSet ListCell...");
        if (empty) {
            log.warn("Item is empty. Setting text to null...");
            this.setText(null);
        } else {
            this.setText(gset.getTitle());

            // Build Tooltip
            Tooltip.install(this, this.buildTooltip(gset));
        }
    }

    public Tooltip buildTooltip(GraphSet gset) {
        log.debug("Building tooltip...");

        StringBuilder lowerCharsSb = new StringBuilder();
        StringBuilder upperCharsSb = new StringBuilder();

        Grapheme[] graphemes = gset.getGraphemes();
        Arrays.stream(graphemes)
                .forEach(g -> {
                    if (g.getType() == GraphemeType.SEMANTIC) {
                        lowerCharsSb.append(g.getLower());
                        upperCharsSb.append(g.getUpper());
                    }
                });

        boolean hasStop = Arrays.stream(graphemes)
                .anyMatch(g -> g.getType() == GraphemeType.STOP);
        boolean hasSemistop = Arrays.stream(graphemes)
                .anyMatch(g -> g.getType() == GraphemeType.LINKING);
        boolean hasNumeric = Arrays.stream(graphemes)
                .anyMatch(g -> g.getType() == GraphemeType.NUMERIC);

        String tooltipString = String.format("Lower Characters: %s\nUpper Characters: %s\n\nHas Stop Characters? %b\nHas Semistop Characters? %b\nHas Numeric Characters? %b",
                lowerCharsSb.toString(),
                upperCharsSb.toString(),
                hasStop, hasSemistop, hasNumeric
                );

        Tooltip tooltip = new Tooltip(tooltipString);
        return tooltip;
    }
}
