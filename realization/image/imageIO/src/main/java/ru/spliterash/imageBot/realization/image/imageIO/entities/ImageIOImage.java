package ru.spliterash.imageBot.realization.image.imageIO.entities;

import lombok.Getter;
import ru.spliterash.imageBot.domain.entities.DomainImage;
import ru.spliterash.imageBot.domain.exceptions.ImageIOException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

@Getter
public class ImageIOImage implements DomainImage {
    private final BufferedImage bufferedImage;

    public ImageIOImage(BufferedImage image) {
        this.bufferedImage = image;
    }

    @Override
    public InputStream read() {
        try {
            PipedInputStream in = new PipedInputStream();
            final PipedOutputStream out = new PipedOutputStream(in);

            new Thread(() -> {
                try (out) {
                    ImageIO.write(bufferedImage, "png", out);
                    out.flush();
                } catch (IOException ex) {
                    throw new ImageIOException(ex);
                }
            }).start();

            return in;
        } catch (IOException ex) {
            throw new ImageIOException(ex);
        }
    }

    @Override
    public int getWidth() {
        return bufferedImage.getWidth();
    }

    @Override
    public int getHeight() {
        return bufferedImage.getHeight();
    }
}
