package com.erayerdin.corpustk.models.corpus;

import com.erayerdin.corpustk.models.Model;
import com.erayerdin.corpustk.models.graphology.GraphSet;
import com.erayerdin.linglib.corpus.Query;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.io.*;

@Log4j2
@ToString(exclude = {"texts", "graphSet", "filteredTexts", "queries"})
public class Corpus implements Model {
    public static final long serialVersionUID = 1L;

    private StringProperty title;
    private ObservableList<Text> texts;
    private ObjectProperty<GraphSet> graphSet;

    @Getter private transient ObservableList<Text> filteredTexts;
    @Getter private transient ObservableList<Query> queries;
    @Getter @Setter private transient File fileOnDisk;

    public Corpus() {
        this.filteredTexts = FXCollections.observableArrayList();
        this.queries = FXCollections.observableArrayList();
        this.fileOnDisk = null;
    }

    public Corpus(String title, GraphSet graphSet) {
        this.title = new SimpleStringProperty(title);
        this.texts = FXCollections.observableArrayList();
        this.graphSet = new SimpleObjectProperty<>(graphSet);

        this.filteredTexts = FXCollections.observableArrayList();
        this.queries = FXCollections.observableArrayList();
        this.fileOnDisk = null;
        log.debug(String.format("Created %s.", this.toString()));
    }

    public void createListeners() {
        // On GraphSet change
        this.graphSet.addListener((prop, oldVal, newVal) -> {
            log.debug(String.format("GraphSet of Corpus changed from %s to %s.", oldVal.toString(), newVal.toString()));
            this.texts.stream()
                    .forEach(t -> t.pretokenize(newVal));
        });
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public ObservableList<Text> getTexts() {
        return texts;
    }

    public void setTexts(ObservableList<Text> texts) {
        this.texts = texts;
    }

    public GraphSet getGraphSet() {
        return graphSet.get();
    }

    public ObjectProperty<GraphSet> graphSetProperty() {
        return graphSet;
    }

    public void setGraphSet(GraphSet graphSet) {
        this.graphSet.set(graphSet);
    }

    @Override
    public void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        log.debug(String.format("Reading (object) %d...", this.hashCode()));
        this.title = new SimpleStringProperty(ois.readUTF());
        this.texts = FXCollections.observableArrayList((Text[]) ois.readObject());
        this.graphSet = new SimpleObjectProperty<>((GraphSet) ois.readObject());

    }

    @Override
    public void writeObject(ObjectOutputStream oos) throws IOException {
        log.debug(String.format("Writing (object) %s...", this.toString()));
        oos.writeUTF(this.getTitle()); // Title as UTF
        oos.writeObject(new Text[this.getTexts().size()]); // Text as Text[]
        oos.writeObject(this.getGraphSet()); // GraphSet
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        log.debug(String.format("Reading (external) %d...", this.hashCode()));
        this.title = new SimpleStringProperty(in.readUTF());
        this.texts = FXCollections.observableArrayList((Text[]) in.readObject());
        this.graphSet = new SimpleObjectProperty<>((GraphSet) in.readObject());
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        log.debug(String.format("Writing (external) %s...", this.toString()));
        out.writeUTF(this.getTitle());
        out.writeObject(this.getTexts().toArray(new Text[this.getTexts().size()]));
        out.writeObject(this.getGraphSet());
    }
}
