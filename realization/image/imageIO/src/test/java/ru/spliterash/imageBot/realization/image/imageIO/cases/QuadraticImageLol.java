package ru.spliterash.imageBot.realization.image.imageIO.cases;

import org.junit.jupiter.api.Test;
import ru.spliterash.imageBot.realization.image.utils.obj.Coords;
import ru.spliterash.imageBot.realization.image.utils.obj.RectangleCoords;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QuadraticImageLol {
    @Test
    public void createQuadratImage() throws IOException {
        BufferedImage image = new BufferedImage(16 * 2, 16 * 2, BufferedImage.TYPE_INT_RGB);

        RectangleCoords coords = new RectangleCoords(
                new Coords(0, 0),
                new Coords(image.getWidth() - 1, image.getHeight() - 1)
        );

        coords.draw(image);

        ImageIO.write(image, "png", new File("quadraticImage.png"));
    }
}
