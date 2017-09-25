package com.erayerdin.corpustk.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MainView extends View {
    private String viewName = "MainScreen";
    private StringProperty title = new SimpleStringProperty("Corpus Toolkit");
    private Integer width = 960;
    private Integer height = 540;

    public MainView() {
        this.setViewName("MainScreen");
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
