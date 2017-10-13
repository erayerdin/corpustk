package com.erayerdin.corpustk.models.graphology;

import com.erayerdin.corpustk.models.Model;
import com.erayerdin.linglib.graphology.Grapheme;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.io.*;

@Log4j2
@ToString(exclude = {"graphemes"})
public class GraphSet extends com.erayerdin.linglib.graphology.GraphSet implements Model {
    public static final long serialVersionUID = 1L;

    private String title;

    public GraphSet() {}

    public GraphSet(Grapheme... graphemes) {
        super(graphemes);
    }

    public GraphSet(String title, Grapheme... graphemes) {
        super(graphemes);
        this.title = title;
    }

    public boolean equals(GraphSet gset) {
        boolean r = true;

        boolean titleEquals = this.getTitle().equals(gset.getTitle());
        if (!titleEquals) return false;

        boolean graphemeLengthEquals = this.getGraphemes().length == gset.getGraphemes().length;
        if (!graphemeLengthEquals) return false;

        for (int i = 0 ; i < this.getGraphemes().length ; i++) {
            Grapheme g1 = this.getGraphemes()[i];
            Grapheme g2 = this.getGraphemes()[i];

            if (!(g1.getLower() == g2.getLower() && g1.getUpper() == g2.getUpper() && g1.getType() == g2.getType())) return false;
        }

        return r;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        log.debug(String.format("Reading (object) %d...", this.hashCode()));
        this.title = ois.readUTF();
        this.setGraphemes((Grapheme[]) ois.readObject());
    }

    @Override
    public void writeObject(ObjectOutputStream oos) throws IOException {
        log.debug(String.format("Writing (object) %s...", this.toString()));
        oos.writeUTF(this.title);
        oos.writeObject(this.getGraphemes());
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        log.debug(String.format("Writing %s...", this.toString()));
        out.writeUTF(this.title);
        out.writeObject(this.getGraphemes());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        log.debug(String.format("Reading %d...", this.hashCode()));
        this.title = in.readUTF();
        this.setGraphemes((Grapheme[]) in.readObject());
    }
}
