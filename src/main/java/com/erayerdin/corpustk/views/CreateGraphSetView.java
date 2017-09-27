package com.erayerdin.corpustk.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CreateGraphSetView extends View {
    private String viewName = "CreateCorpusPackageScreen";
    private StringProperty title = new SimpleStringProperty("Create GraphSet");
    private Integer width = 960;
    private Integer height = 540;

    public CreateGraphSetView() {
        this.setViewName("CreateGraphSetScreen");
        this.setDecorated(true);
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
