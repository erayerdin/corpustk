package com.erayerdin.corpustk.core.tablecells;

import com.erayerdin.linglib.corpus.Query;
import javafx.scene.control.TableCell;
import lombok.extern.log4j.Log4j2;

// https://rterp.wordpress.com/2013/07/08/using-a-custom-tablecell-factory-to-format-a-javafx-table-in-fxml/
@Log4j2
public class QueryTableCell extends TableCell<String, Query> { // TODO continue later on
    public void updateItem(Query query, boolean empty) {
        super.updateItem(query, empty);

        log.debug("Updating Query TableCell...");
    }
}
