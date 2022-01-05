package ru.spliterash.imageBot.realization.image.imageIO.cases;

import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.cases.CoverImageUseCase;
import ru.spliterash.imageBot.domain.cases.ResizeCase;
import ru.spliterash.imageBot.domain.def.CaseExecutor;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.realization.image.imageIO.entities.ImageIOImageData;
import ru.spliterash.imageBot.realization.image.imageIO.utils.ImageIOUtils;
import ru.spliterash.imageBot.realization.image.utils.obj.Coords;

import java.awt.*;
import java.awt.image.BufferedImage;

@RequiredArgsConstructor
public class ImageIOCoverImageCase extends CoverImageUseCase {
    private final CaseExecutor executor;
    private final ResizeCase resizeCase;


    @Override
    public ImageData process(ImageData inputImage, Input params) {
        BufferedImage result = new BufferedImage(params.getWidth(), params.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // Пытаемся найти вариант в котором будет меньше всего "потерь" ну или как настройки забиты

        double finalProportion;

        double widthProportion = (double) result.getWidth() / (double) inputImage.getWidth();
        double heightProportion = (double) result.getHeight() / (double) inputImage.getHeight();

        // Лучше работать по большой, поэтому ищем её

        if (params.isCutImage())
            finalProportion = Math.max(widthProportion, heightProportion);
        else
            finalProportion = Math.min(widthProportion, heightProportion);


        BufferedImage buffImage = ImageIOUtils.loadImage(executor.execute(resizeCase, inputImage, new ResizeCase.Input(finalProportion)));

        Coords drawTo = new Coords(
                result.getWidth() / 2,
                result.getHeight() / 2
        ).add(-(buffImage.getWidth() / 2), -(buffImage.getHeight() / 2));

        Graphics2D g = result.createGraphics();

        g.drawImage(buffImage, drawTo.getX(), drawTo.getY(), buffImage.getWidth(), buffImage.getHeight(), null);
        g.dispose();

        return new ImageIOImageData(result);
    }

}
