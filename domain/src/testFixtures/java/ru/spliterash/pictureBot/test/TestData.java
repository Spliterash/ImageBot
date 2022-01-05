package ru.spliterash.pictureBot.test;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class TestData {
    @SneakyThrows
    public List<InputStream> getCatsImages() {
        return getPathsFromResourceJAR("/cats")
                .stream()
                .map(path -> {
                    URL resource = TestData.class.getClassLoader().getResource(path.toString().substring(1));
                    try {
                        return resource.openStream();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    public InputStream quadraticImage() {
        return TestData.class.getClassLoader().getResourceAsStream("quadraticImage.png");
    }

    private List<Path> getPathsFromResourceJAR(String folder)
            throws URISyntaxException, IOException {

        List<Path> result;

        // get path of the current running JAR
        String jarPath = TestData.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath();

        // file walks JAR
        URI uri = URI.create("jar:file:" + jarPath);
        try (FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
            result = Files.walk(fs.getPath(folder))
                    .filter(Files::isRegularFile)
                    .sorted(Comparator.comparing(Path::toString))
                    .collect(Collectors.toList());
        }

        return result;

    }
}
