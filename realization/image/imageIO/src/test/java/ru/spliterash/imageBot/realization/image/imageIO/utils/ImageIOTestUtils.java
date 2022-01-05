package ru.spliterash.imageBot.realization.image.imageIO.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;
import ru.spliterash.imageBot.domain.entities.DomainImage;
import ru.spliterash.imageBot.realization.image.imageIO.entities.ImageIOImage;
import ru.spliterash.pictureBot.test.TestData;

import javax.imageio.ImageIO;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ImageIOTestUtils {
    public List<DomainImage> loadCats() {
        return TestData.getCatsImages().stream()
                .map(ImageIOTestUtils::loadFromFile)
                .collect(Collectors.toList());
    }

    public DomainImage loadQuadratic() {
        return ImageIOTestUtils.loadFromFile(TestData.quadraticImage());
    }

    @SneakyThrows
    public DomainImage loadFromFile(InputStream stream) {
        return new ImageIOImage(ImageIO.read(stream));
    }


    @SneakyThrows
    public void saveImage(DomainImage image, String name) {
        FileOutputStream stream = new FileOutputStream(name);

        IOUtils.copy(image.read(), stream);
    }
}
