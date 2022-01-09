package ru.spliterash.imageBot.realization.image.imageIO.cases;

import ru.spliterash.imageBot.domain.cases.ResizeCase;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.realization.image.imageIO.entities.ImageIOImageData;
import ru.spliterash.imageBot.realization.image.imageIO.utils.ImageIOUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageIOResizeCase extends ResizeCase {
    @Override
    public ImageData process(ImageData inputImage, Input params) {
        if (params.getProportion() == 1)
            return inputImage;

        int newX = (int) Math.round((double) inputImage.getWidth() * params.getProportion());
        int newY = (int) Math.round((double) inputImage.getHeight() * params.getProportion());

        int type = BufferedImage.TYPE_INT_ARGB;

        BufferedImage image = ImageIOUtils.loadImage(inputImage);

        BufferedImage resizedImage = new BufferedImage(newX, newY, type);
        Graphics2D g = resizedImage.createGraphics();

        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(image, 0, 0, newX, newY, null);
        g.dispose();

        return new ImageIOImageData(resizedImage);
    }
}
