package com.erayerdin.corpustk.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TextView extends View {
    private String viewName = "TextScreen";
    private StringProperty title = new SimpleStringProperty("View/Edit Text");
    private Integer width = 960;
    private Integer height = 540;

    public TextView() {
        this.setViewName("TextScreen");
        this.setDecorated(true);
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
