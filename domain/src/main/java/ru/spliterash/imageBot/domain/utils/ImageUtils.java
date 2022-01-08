package ru.spliterash.imageBot.domain.utils;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.domain.entities.defaultEnt.FileImage;
import ru.spliterash.imageBot.domain.exceptions.CaseErrorException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class ImageUtils {
    public static Dimension getImageDimension(InputStream image) throws IOException {
        ImageInputStream stream = ImageIO.createImageInputStream(image);
        Iterator<ImageReader> iter = ImageIO.getImageReaders(stream);

        while (iter.hasNext()) {
            ImageReader reader = iter.next();
            try {
                reader.setInput(stream);
                int width = reader.getWidth(reader.getMinIndex());
                int height = reader.getHeight(reader.getMinIndex());
                return new Dimension(width, height);
            } catch (IOException ignored) {
            } finally {
                reader.dispose();
            }
        }

        throw new CaseErrorException("Ошибка получения размера картинки");
    }


    @SneakyThrows
    public static File getImageFile(ImageData data) {
        if (data instanceof FileImage)
            return ((FileImage) data).getFile();

        File tempFile = File.createTempFile("imageBotImage", ".png");
        tempFile.deleteOnExit();

        IOUtils.copy(data.read(), new FileOutputStream(tempFile));

        return tempFile;
    }
}
