package com.erayerdin.corpustk.core.listcells;

import com.erayerdin.corpustk.models.corpus.Text;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import lombok.extern.log4j.Log4j2;

// https://www.billmann.de/2013/07/03/javafx-custom-listcell/
@Log4j2
public class TextListCell extends ListCell<Text> {

    public void updateItem(Text text, boolean empty) {
        super.updateItem(text, empty);

        log.debug("Building Text ListCell...");
        if (empty) {
            log.warn("Item is empty. Setting text to null...");
            this.setText(null);
        } else {
            this.setText(text.getContent().substring(0, 10)+"...");

            // Build Tooltip
            if (!text.getTags().isEmpty()) {
                Tooltip.install(this, this.buildTooltip(text));
            } else {
                log.warn("Text does not have tags. Not building tooltip.");
            }
        }
    }

    public Tooltip buildTooltip(Text text) {
        log.debug("Building tooltip...");

        StringBuilder sb = new StringBuilder();
        String[] tags = text.getTags().toArray(new String[text.getTags().size()]);

        sb.append("Tags\n");

        for (String tag : tags) {
            sb.append("["+tag+"]\n");
        }

        String tooltipString = sb.toString().trim();
        Tooltip tooltip = new Tooltip(tooltipString);

        return tooltip;
    }
}
