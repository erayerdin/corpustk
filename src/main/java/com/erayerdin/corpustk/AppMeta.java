package com.erayerdin.corpustk;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

enum Status {
    SNAPSHOT,
    ALPHA,
    BETA,
    STABLE;
}

@Log4j2
public class AppMeta {
    @Getter private final static String humanReadableLabel = "Corpus Toolkit";
    @Getter private final static String machineReadableLabel = "corpustk";
    @Getter private final static String groupID = "com.erayerdin";
    @Getter private final static String artifactID = "corpustk";

    @Getter private final static int majorVersion = 0;
    @Getter private final static int minorVersion = 2;
    @Getter private final static int patchVersion = 1;
    @Getter private final static Status versionStatus = Status.SNAPSHOT;

    @Getter private static HashMap<String, String[]> contributors = initContributors();
    @Getter private static String description = initDescription();
    @Getter private static String license = initLicense();

    public static String generateVersionString() {
        StringBuilder sb = new StringBuilder();
        sb.append(majorVersion);
        sb.append(".");
        sb.append(minorVersion);
        sb.append(".");
        sb.append(patchVersion);

        switch (versionStatus) {
            case SNAPSHOT:
                sb.append("-SNAPSHOT");
                break;
            case ALPHA:
                sb.append("a");
                break;
            case BETA:
                sb.append("b");
                break;
            case STABLE:
                break;
        }

        return sb.toString();
    }

    public static String getBasePackage() {
        String basePackage = groupID + "." + artifactID;
        return basePackage;
    }

    private static HashMap<String, String[]> initContributors() {
        log.debug("Initializing HashMap for contributors...");
        HashMap<String, String[]> allContributors = new HashMap<>();

        log.debug("Adding contributors...");
        allContributors.put(
                "Eray Erdin <eraygezer.94@gmail.com>",
                new String[] {
                        "Main Developer"
                }
        );

        return allContributors;
    }

    private static String initDescription() {
        log.debug("Reading description file...");
        String desc = null;
        try {
            // https://stackoverflow.com/questions/20389255/reading-a-resource-file-from-within-jar
            ClassLoader classLoader = AppMeta.class.getClassLoader();
//            File file = new File(classLoader.getResource("DESCRIPTION.txt").getFile());
            InputStream inStream = classLoader.getResourceAsStream("DESCRIPTION.txt");
//            FileInputStream fileInStream = new FileInputStream(file);
//            BufferedReader buffer = new BufferedReader(new InputStreamReader(fileInStream));
            BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
            StringBuilder builder = new StringBuilder();

            String line;

            while ((line = buffer.readLine()) != null) {
                builder.append(line+"\n");
            }

            buffer.close();
            inStream.close();

            desc = builder.toString();
        } catch (IOException e) {
            log.error("An error occured while reading description file...", e);
        }
        return desc;
    }

    private static String initLicense() {
        log.debug("Reading license file...");
        String lcs = null;
        try {
            // https://stackoverflow.com/questions/20389255/reading-a-resource-file-from-within-jar
            ClassLoader classLoader = AppMeta.class.getClassLoader();
//            File file = new File(classLoader.getResource("DESCRIPTION.txt").getFile());
            InputStream inStream = classLoader.getResourceAsStream("LICENSE.txt");
//            FileInputStream fileInStream = new FileInputStream(file);
//            BufferedReader buffer = new BufferedReader(new InputStreamReader(fileInStream));
            BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
            StringBuilder builder = new StringBuilder();

            String line;

            while ((line = buffer.readLine()) != null) {
                builder.append(line+"\n");
            }

            buffer.close();
            inStream.close();

            lcs = builder.toString();
        } catch (IOException e) {
            log.error("An error occured while reading license file...", e);
        }
        return lcs;
    }
}