package com.erayerdin.corpustk.models.graphology;

import com.erayerdin.linglib.graphology.GraphemeType;
import com.erayerdin.linglib.graphology.exceptions.InvalidCharSequenceLengthException;

public class GraphSetFactory {
    public static GraphSet createGraphSet(String lower, String upper, GraphemeType... types) throws InvalidCharSequenceLengthException {
        com.erayerdin.linglib.graphology.GraphSet gsetBase = com.erayerdin.linglib.graphology.GraphSetFactory.createGraphSet(lower, upper, types);
        GraphSet gset = new GraphSet(gsetBase.getGraphemes());
        return gset;
    }
}
