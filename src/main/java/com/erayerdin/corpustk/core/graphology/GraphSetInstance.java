package com.erayerdin.corpustk.core.graphology;

import com.erayerdin.corpustk.App;
import com.erayerdin.corpustk.AppMeta;
import com.erayerdin.linglib.graphology.GraphSet;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.io.*;

@Log4j2
@ToString(exclude = "graphSet")
public class GraphSetInstance implements Serializable {
    private StringProperty title;
    private ObjectProperty<GraphSet> graphSet;

    public GraphSetInstance(String title, GraphSet graphSet) {
        log.debug("Init<GraphSetInstance>@%d", this.hashCode());
        this.title = new SimpleStringProperty(title);
        this.graphSet = new SimpleObjectProperty<GraphSet>(graphSet);
    }

    public GraphSet getGraphSet() {
        return graphSet.get();
    }

    public ObjectProperty<GraphSet> graphSetProperty() {
        return graphSet;
    }

    public void setGraphSet(GraphSet graphSet) {
        this.graphSet.set(graphSet);
        log.debug("%s || graphset set", this.toString());
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void save() throws IOException {
        log.debug(String.format("Serializing object: %s", this.toString()));
        FileOutputStream fileOutputStream = new FileOutputStream(new File(App.getGraphsetDir().getAbsolutePath(), this.title.get().toLowerCase()+".gset"));
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(this);
        objectOutputStream.close();
        fileOutputStream.close();
    }

    public static GraphSetInstance load(File file) throws IOException, ClassNotFoundException {
        log.debug(String.format("Deserializing object: %s", file.toString()));
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        GraphSetInstance graphSetInstance = (GraphSetInstance) objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();

        return graphSetInstance;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.title = new SimpleStringProperty(objectInputStream.readUTF());
        this.graphSet = new SimpleObjectProperty<GraphSet>((GraphSet) objectInputStream.readObject());
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeUTF(this.getTitle());
        objectOutputStream.writeObject(this.getGraphSet());
    }
}
