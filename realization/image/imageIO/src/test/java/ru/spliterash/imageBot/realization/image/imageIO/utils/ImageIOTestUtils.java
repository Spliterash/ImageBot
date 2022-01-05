package ru.spliterash.imageBot.realization.image.imageIO.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.realization.image.imageIO.entities.ImageIOImageData;
import ru.spliterash.pictureBot.test.TestData;

import javax.imageio.ImageIO;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ImageIOTestUtils {
    public List<ImageData> loadCats() {
        return TestData.getCatsImages().stream()
                .map(ImageIOTestUtils::loadFromFile)
                .collect(Collectors.toList());
    }

    public ImageData loadQuadratic() {
        return ImageIOTestUtils.loadFromFile(TestData.quadraticImage());
    }

    @SneakyThrows
    public ImageData loadFromFile(InputStream stream) {
        return new ImageIOImageData(ImageIO.read(stream));
    }


    @SneakyThrows
    public void saveImage(ImageData image, String name) {
        FileOutputStream stream = new FileOutputStream(name);

        IOUtils.copy(image.read(), stream);
    }
}
