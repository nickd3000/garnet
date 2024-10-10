package com.physmo.garnet;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    // TODO: move to file utils?
    public static InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = Utils.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    public static String getPathForResource(Object me, String resourceName) {

        URL resource = me.getClass().getClassLoader().getResource(resourceName);

        if (resource == null) {
            throw new RuntimeException("File not found: [" + resourceName + "]");
        }

        File file = null;
        try {
            Path path = Paths.get(resource.toURI());
            file = path.toFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (file != null)
            return file.getAbsolutePath();
        else
            return null;
    }
}
