package com.erayerdin.corpustk.models.corpus;
;
import com.erayerdin.corpustk.models.Model;
import com.erayerdin.corpustk.models.graphology.GraphSet;
import com.erayerdin.linglib.corpus.Token;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.util.Arrays;

@Log4j2
public class Text extends com.erayerdin.linglib.corpus.Text implements Model {
    public static final long serialVersionUID = 1L;

    private StringProperty content; // TODO not initializing property while readExternal
    @Getter private ObservableList<String> tags;
    private Token[][] tokens;

    public Text() {
        this.content = new SimpleStringProperty(null);
        this.tags = FXCollections.observableArrayList();
    }

    public Text(String content, GraphSet graphSet) {
        this.content = new SimpleStringProperty(content);
        this.tags = FXCollections.observableArrayList();
        log.debug(String.format("Created %s.", this.toString()));
        this.pretokenize(graphSet);
    }

    public Text(String content, GraphSet graphSet, ObservableList<String> tags) {
        this.content = new SimpleStringProperty(content);
        this.tags = tags;
        log.debug(String.format("Created %s.", this.toString()));
        this.pretokenize(graphSet);
    }

    protected void pretokenize(GraphSet graphSet) {
        log.debug(String.format("Pretokenizing %d...", this.hashCode()));
        this.tokens = this.tokenize(graphSet);
    }

    @Override
    public String getContent() {
        return content.get();
    }

    public StringProperty contentProperty() {
        return content;
    }

    public void setContent(String content) {
        this.content.set(content);
    }

    public ObservableList<String> getTags() {
        return tags;
    }

    public void setTags(ObservableList<String> tags) {
        this.tags = tags;
    }

    public Token[][] getTokens() {
        return tokens;
    }

    public void setTokens(Token[][] tokens) {
        this.tokens = tokens;
    }

    public void writeObject(ObjectOutputStream oos) throws IOException {
        log.debug(String.format("Writing (object) %d...", this.hashCode()));

        oos.writeUTF(this.getContent());
        oos.writeObject(this.getTags().toArray());
        oos.writeObject(this.getTokens());
    }

    public void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        log.debug(String.format("Reading (object) %d...", this.hashCode()));

        this.setContent(ois.readUTF());
        Object[] tags = (Object[]) ois.readObject();
        this.tags = FXCollections.observableArrayList(
                Arrays.copyOf(tags, tags.length, String[].class) // convert to String[]
        );
        this.tokens = (Token[][]) ois.readObject();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        log.debug(String.format("Writing (external) %d...", this.hashCode()));

        out.writeUTF(this.getContent());
        out.writeObject(this.getTags().toArray(new String[this.getTags().size()]));
        out.writeObject(this.getTokens());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        log.debug(String.format("Reading (external) %d...", this.hashCode()));

        this.content = new SimpleStringProperty(in.readUTF());
        this.tags = FXCollections.observableArrayList((String[]) in.readObject());
        this.tokens = (Token[][]) in.readObject();
    }
}
