package com.physmo.garnet;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility methods for loading files and resources from the classpath.
 */
public class FileUtils {
    /**
     * Opens a classpath resource as an {@link InputStream}.
     *
     * @param fileName the classpath-relative resource path (e.g. {@code "shaders/blur_h.frag"})
     * @return an {@link InputStream} for the resource
     * @throws IllegalArgumentException if the resource cannot be found
     */
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

    /**
     * Resolves a classpath resource to an absolute filesystem path.
     *
     * @param me           any object whose class loader will be used to locate the resource
     * @param resourceName the classpath-relative resource name
     * @return the absolute path to the resource file, or {@code null} if the path cannot be resolved
     * @throws RuntimeException if the resource is not found on the classpath
     */
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
