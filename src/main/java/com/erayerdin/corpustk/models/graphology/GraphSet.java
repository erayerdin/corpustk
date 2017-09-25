package com.erayerdin.corpustk.models.graphology;

import com.erayerdin.linglib.graphology.Grapheme;

public class GraphSet extends com.erayerdin.linglib.graphology.GraphSet {
    private String title;

    public GraphSet(Grapheme... graphemes) {
        super(graphemes);
    }

    public GraphSet(String title, Grapheme... graphemes) {
        super(graphemes);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
