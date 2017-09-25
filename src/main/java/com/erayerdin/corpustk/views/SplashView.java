package com.erayerdin.corpustk.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SplashView extends View {
    private String viewName = "SplashScreen";
    private StringProperty title = new SimpleStringProperty("");
    private Integer width = 960;
    private Integer height = 540;
    private boolean decorated = false;

    public SplashView() {
        this.setViewName("SplashScreen");
        this.setDecorated(false);
    }
}
