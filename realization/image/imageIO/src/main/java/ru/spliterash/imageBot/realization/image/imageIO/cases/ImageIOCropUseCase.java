package ru.spliterash.imageBot.realization.image.imageIO.cases;

import ru.spliterash.imageBot.domain.cases.CropImageUseCase;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.realization.image.imageIO.entities.ImageIOImageData;
import ru.spliterash.imageBot.realization.image.imageIO.utils.ImageIOUtils;

import java.awt.image.BufferedImage;

public class ImageIOCropUseCase extends CropImageUseCase {
    @Override
    public ImageData process(ImageData image, Params params) {
        int up = 0, down = 0, left = 0, right = 0;

        if (params.getAll() != null) {
            up = params.getAll();
            down = params.getAll();
            left = params.getAll();
            right = params.getAll();
        }

        if (params.getTop() != null)
            up = params.getTop();

        if (params.getBottom() != null)
            down = params.getBottom();

        if (params.getLeft() != null)
            left = params.getLeft();

        if (params.getRight() != null)
            right = params.getRight();

        if (up == 0 && down == 0 && left == 0 && right == 0)
            return image;

        BufferedImage buff = ImageIOUtils.loadImage(image);

        int x, y, width, height;

        x = left;
        y = up;
        width = buff.getWidth() - (right * 2);
        height = buff.getHeight() - (down * 2);

        return new ImageIOImageData(buff.getSubimage(x, y, width, height));
    }
}
