package ru.spliterash.imageBot.realization.image.imageIO.utils;

import lombok.experimental.UtilityClass;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.domain.exceptions.ImageReadError;
import ru.spliterash.imageBot.realization.image.imageIO.entities.ImageIOImageData;
import ru.spliterash.imageBot.realization.image.utils.obj.Coords;
import ru.spliterash.imageBot.realization.image.utils.obj.RectangleCoords;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ImageIOUtils {
    public BufferedImage loadImage(ImageData image) {
        if (image instanceof ImageIOImageData) // Небольшая оптимизация, если хранилище картинки и так ImageIO
            return ((ImageIOImageData) image).getBufferedImage();
        else {
            try {
                return ImageIO.read(image.read());
            } catch (IOException e) {
                throw new ImageReadError(e);
            }
        }
    }

    public List<BufferedImage> loadImages(List<ImageData> images) {
        return images
                .stream()
                .map(ImageIOUtils::loadImage)
                .collect(Collectors.toList());
    }

    public void draw(Graphics2D g, RectangleCoords rectangle) {
        Coords a = rectangle.getA();
        g.fill(new Rectangle(a.getX(), a.getY(), rectangle.getWidth(), rectangle.getHeight()));
    }

}
