package com.erayerdin.corpustk.core.corpus;

import com.erayerdin.corpustk.core.graphology.GraphSetInstance;
import com.erayerdin.linglib.corpus.Text;
import com.erayerdin.linglib.corpus.Token;
import com.erayerdin.linglib.graphology.GraphSet;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@Log4j2
@ToString(exclude = {"text", "tokens"})
public class TextInstance {
    private ObjectProperty<Text> text;
    private ObservableList<String> tags;
    private Token[][] tokens;

    public TextInstance(Text text, GraphSetInstance graphSetInstance) {
        log.debug("Init<TextInstance>@%d", this.hashCode());

        this.text = new SimpleObjectProperty<Text>(text);
        this.tags = null;

        log.debug("Initializing tags...");
        this.tags = FXCollections.observableArrayList();
        this.pretokenize(graphSetInstance.getGraphSet());
    }

    private void pretokenize(GraphSet graphSet) {
        log.debug("Starting pretokenization process...");
        Token[][] tokens = this.getText().tokenize(graphSet);
        this.tokens = new Token[tokens.length][];

        for (int i=0 ; i<tokens.length ; i++) {
            Token[] section = tokens[i];
            this.tokens[i] = section;
        }
    }

    public Text getText() {
        return text.get();
    }

    public ObjectProperty<Text> textProperty() {
        return text;
    }

    public void setText(Text text, GraphSet graphSet) {
        this.text.set(text);
        log.debug("%s || text set", this.toString());
        this.pretokenize(graphSet);
    }

    public ObservableList<String> getTags() {
        return tags;
    }

    public void setTags(ObservableList<String> tags) {
        this.tags = tags;
        log.debug("%s || tags set", this.toString());
    }

    public Token[][] getTokens() {
        return tokens;
    }

//    public void setTokens(Token[][] tokens) {
//        this.tokens = tokens;
//    }

    public void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.text = new SimpleObjectProperty<Text>((Text) objectInputStream.readObject());
        this.tags = (ObservableList<String>) objectInputStream.readObject();
        this.tokens = (Token[][]) objectInputStream.readObject();
    }

    public void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.getText());
        objectOutputStream.writeObject(this.getTags());
        objectOutputStream.writeObject(this.getTokens());
    }
}