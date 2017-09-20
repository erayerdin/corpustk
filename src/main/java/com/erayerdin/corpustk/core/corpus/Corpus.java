package com.erayerdin.corpustk.core.corpus;

import com.erayerdin.corpustk.core.graphology.GraphSetInstance;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.util.ArrayList;

@Log4j2
@ToString(exclude = "textInstances")
public class Corpus implements java.io.Serializable {
    private StringProperty title;
    private ObservableList<TextInstance> textInstances;
    private ObjectProperty<GraphSetInstance> graphSetInstance;

    public Corpus(String title, GraphSetInstance graphSetInstance) {
        log.debug("Init<Corpus>@%d", this.hashCode());

        this.title = new SimpleStringProperty(title);
        this.graphSetInstance = new SimpleObjectProperty<GraphSetInstance>(graphSetInstance);

        log.debug("Initializing text instance array...");
        this.textInstances = FXCollections.observableArrayList();
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
        log.debug("%s || title set to %s", this.toString(), title);
    }

    public ObservableList<TextInstance> getTextInstances() {
        return textInstances;
    }

    public void setTextInstances(ObservableList<TextInstance> textInstances) {
        this.textInstances = textInstances;
        log.debug("%s || text instance set", this.toString());
    }

    public GraphSetInstance getGraphSetInstance() {
        return graphSetInstance.get();
    }

    public ObjectProperty<GraphSetInstance> graphSetInstanceProperty() {
        return graphSetInstance;
    }

    public void setGraphSetInstance(GraphSetInstance graphSetInstance) {
        this.graphSetInstance.set(graphSetInstance);
        log.debug("%s || graphset instance set to %s", this.toString(), graphSetInstance.toString());
    }

    public void save(File file) throws IOException {
        log.debug(String.format("Saving corpus file to %s", file.getName()));
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(this);
        objectOutputStream.close();
        fileOutputStream.close();
    }

    public static Corpus load(File file) throws IOException, ClassNotFoundException {
        Corpus corpus = null;
        log.debug(String.format("Loading corpus file from %s", file.getName()));
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        corpus = (Corpus) objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();
        return corpus;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.title = new SimpleStringProperty(objectInputStream.readUTF());
        ArrayList<TextInstance> textInstances = (ArrayList<TextInstance>) objectInputStream.readObject();
        this.textInstances = (ObservableList<TextInstance>) textInstances;
        this.graphSetInstance = new SimpleObjectProperty<GraphSetInstance>((GraphSetInstance) objectInputStream.readObject());
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeUTF(this.getTitle());
        objectOutputStream.writeObject(new ArrayList<TextInstance>(this.getTextInstances()));
        objectOutputStream.writeObject(this.getGraphSetInstance());
    }
}
