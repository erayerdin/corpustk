package com.erayerdin.corpustk.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CreateCorpusPackageView extends View {
    private String viewName = "CreateCorpusPackageScreen";
    private StringProperty title = new SimpleStringProperty("Create New Corpus Package");
    private Integer width = 960;
    private Integer height = 540;

    public CreateCorpusPackageView() {
        this.setViewName("CreateCorpusPackageScreen");
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
