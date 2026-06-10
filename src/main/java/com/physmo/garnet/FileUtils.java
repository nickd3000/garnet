package com.physmo.garnet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility methods for loading files and resources from the classpath.
 */
public class FileUtils {
    private static final Path EXAMPLE_RESOURCES_DIRECTORY = Paths.get("src", "main", "resources_examples");

    /**
     * Opens a classpath resource as an {@link InputStream}. When running from a
     * development checkout, this also falls back to {@code src/main/resources_examples}
     * so reference examples can load their non-packaged assets.
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
        if (inputStream != null) {
            return inputStream;
        }

        Path exampleResourcePath = EXAMPLE_RESOURCES_DIRECTORY.resolve(fileName);
        if (Files.isRegularFile(exampleResourcePath)) {
            try {
                return Files.newInputStream(exampleResourcePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to open file: " + exampleResourcePath, e);
            }
        }

        throw new IllegalArgumentException("file not found! " + fileName);
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
