package com.github.davigon.utils;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Location {
    public static Path JAR_PATH;

    static {
        try {
            JAR_PATH = Paths.get((new File(
                    Location.class
                            .getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI()))
                    .getParentFile()
                    .getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private Location() {

    }
}
