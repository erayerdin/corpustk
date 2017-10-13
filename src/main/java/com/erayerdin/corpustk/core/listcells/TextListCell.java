package com.erayerdin.corpustk.core.listcells;

import com.erayerdin.corpustk.controllers.MainController;
import com.erayerdin.corpustk.controllers.TextController;
import com.erayerdin.corpustk.models.corpus.Text;
import com.erayerdin.corpustk.views.TextView;
import com.erayerdin.corpustk.views.View;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
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
            // Set Initial Text
            this.setText(this.textShortener(text.getContent()));

            // Build Initial Tooltip
            if (!text.getTags().isEmpty()) {
                Tooltip.install(this, this.buildTooltip(text));
            } else {
                log.warn("Text does not have tags. Not building tooltip.");
            }

            // Listener
            text.contentProperty().addListener((prop, oldVal, newVal) -> {
                setText(this.textShortener(newVal));
            });

            this.buildContextMenu();
        }
    }

    private void buildContextMenu() {
        log.debug("Adding context menu to text list cell...");
        ContextMenu menu = new ContextMenu();

        MenuItem viewText = new MenuItem("View Text");
        MenuItem removeText = new MenuItem("Remove Text");

        viewText.setOnAction(e -> {
            Text obj = this.getItem();
            log.debug(String.format("Opening %s text...", obj.toString()));

            TextController.setTextInstance(obj);

            View textView = new TextView();
            Stage stage = textView.createStage();
            stage.showAndWait();
        });

        removeText.setOnAction(e -> {
            MainController.getCorpusInstance().getTexts().remove(this.getItem());
        });

        menu.getItems().addAll(viewText, removeText);

        this.setContextMenu(menu);
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

    private String textShortener(String text) {
        String str = text.trim()
                .replaceAll("\n", "");

        if (str.length() > 30) {
            str = str.substring(0, 27)+"...";
        }

        return str;
    }
}
