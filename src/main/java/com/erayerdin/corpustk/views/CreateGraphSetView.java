package com.erayerdin.corpustk.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CreateGraphSetView extends View {
    private String viewName = "CreateCorpusPackageScreen";
    private StringProperty title = new SimpleStringProperty("Create GraphSet");

    public CreateGraphSetView() {
        this.setViewName("CreateGraphSetScreen");
        this.setModal(true);
        this.setResizable(false);
    }

    @Override
    public String getTitle() {
        return title.get();
    }

    @Override
    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }
}
