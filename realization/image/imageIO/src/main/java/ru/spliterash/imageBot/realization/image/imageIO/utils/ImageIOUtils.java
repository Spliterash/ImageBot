package ru.spliterash.imageBot.realization.image.imageIO.utils;

import lombok.experimental.UtilityClass;
import ru.spliterash.imageBot.domain.entities.DomainImage;
import ru.spliterash.imageBot.domain.exceptions.ImageReadError;
import ru.spliterash.imageBot.realization.image.imageIO.entities.ImageIOImage;
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
    public BufferedImage loadImage(DomainImage image) {
        if (image instanceof ImageIOImage) // Небольшая оптимизация, если хранилище картинки и так ImageIO
            return ((ImageIOImage) image).getBufferedImage();
        else {
            try {
                return ImageIO.read(image.read());
            } catch (IOException e) {
                throw new ImageReadError(e);
            }
        }
    }

    public List<BufferedImage> loadImages(List<DomainImage> images) {
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
