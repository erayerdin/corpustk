package com.erayerdin.corpustk.models.corpus;
;
import com.erayerdin.corpustk.models.graphology.GraphSet;
import com.erayerdin.linglib.corpus.Token;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

@Log4j2
public class Text extends com.erayerdin.linglib.corpus.Text implements Serializable {
    private ObservableList<String> tags;
    private Token[][] tokens;

    public Text(String content, GraphSet graphSet) {
        super(content);
        this.tags = FXCollections.observableArrayList();
        log.debug(String.format("Created %s.", this.toString()));
        this.pretokenize(graphSet);
    }

    public Text(String content, GraphSet graphSet, ObservableList<String> tags) {
        super(content);
        this.tags = tags;
        log.debug(String.format("Created %s.", this.toString()));
        this.pretokenize(graphSet);
    }

    protected void pretokenize(GraphSet graphSet) {
        log.debug(String.format("Pretokenizing %d...", this.hashCode()));
        this.tokens = this.tokenize(graphSet);
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

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeUTF(this.getContent());
        oos.writeObject(this.getTags().toArray());
        oos.writeObject(this.getTokens());
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        this.setContent(ois.readUTF());
        Object[] tags = (Object[]) ois.readObject();
        this.tags = FXCollections.observableArrayList(
                Arrays.copyOf(tags, tags.length, String[].class) // convert to String[]
        );
        this.tokens = (Token[][]) ois.readObject();
    }
}
