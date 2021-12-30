package com.physmo.garnet;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {

    public static String getPathForResource(Object me, String resourceName) {

        URL resource = me.getClass().getResource(resourceName);

        if (resource == null) {
            System.out.println("File not found: " + resourceName);
        }

        File file = null;
        try {
            Path path = Paths.get(resource.toURI());
            file = path.toFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            //System.out.println("File not found: "+resourceName);
        }

        if (file != null)
            return file.getAbsolutePath();
        else
            return null;
    }

}
