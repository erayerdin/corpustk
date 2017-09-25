package com.erayerdin.corpustk.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public interface Model extends Serializable, Externalizable {
    static void save(Model obj, File path) throws IOException {
        Logger logger = LogManager.getLogger(Model.class);
        logger.debug(
                String.format("Saving %s to %s", obj.toString(), path.getAbsolutePath())
        );

        FileOutputStream fileOutputStream = new FileOutputStream(path);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
        fileOutputStream.close();
    }

    static Model load(File path) throws IOException, ClassNotFoundException {
        Logger logger = LogManager.getLogger(Model.class);
        logger.debug(
                String.format("Loading object from %s", path.getAbsolutePath())
        );

        Model obj = null;

        FileInputStream fileInputStream = new FileInputStream(path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        obj = (Model) objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();

        return obj;
    }

    void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException;
    void writeObject(ObjectOutputStream oos) throws IOException;
}
