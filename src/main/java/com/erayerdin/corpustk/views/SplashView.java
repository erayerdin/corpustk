package com.erayerdin.corpustk.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SplashView extends View {
    private String viewName = "SplashScreen";
    private StringProperty title = new SimpleStringProperty(null);
    private Integer width = 960;
    private Integer height = 540;

    public SplashView() {
        super();
        this.setViewName("SplashScreen");
        this.setDecorated(false);
    }
}
