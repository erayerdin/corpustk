package com.erayerdin.corpustk.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AboutView extends View {
    private String viewName = "AboutScreen";
    private StringProperty title = new SimpleStringProperty("About Corpus Toolkit");
    private Integer width = 500;
    private Integer height = 800;

    public AboutView() {
        this.setViewName("AboutScreen");
        this.setDecorated(true);
        this.setResizable(false);
        this.setModal(true);
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
