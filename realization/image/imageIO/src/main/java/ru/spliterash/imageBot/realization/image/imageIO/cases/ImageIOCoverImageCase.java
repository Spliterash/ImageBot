package ru.spliterash.imageBot.realization.image.imageIO.cases;

import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.cases.CoverImageUseCase;
import ru.spliterash.imageBot.domain.cases.ProportionResizeCase;
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
    private final ProportionResizeCase proportionResizeCase;


    @Override
    public ImageData process(ImageData inputImage, Input params) {
        // Пытаемся найти вариант в котором будет меньше всего "потерь" ну или как настройки забиты

        boolean newAspectRatio = false;
        int width, height;
        if (params.getWidth() == -1) {
            height = params.getHeight();
            width = (int) Math.round((double) inputImage.getWidth() * ((double) height / (double) inputImage.getHeight()));
        } else if (params.getHeight() == -1) {
            width = params.getWidth();
            height = (int) Math.round((double) inputImage.getHeight() * ((double) width / (double) inputImage.getWidth()));
        } else {
            width = params.getWidth();
            height = params.getHeight();
            newAspectRatio = true;
        }

        double finalProportion;

        double widthProportion = (double) width / (double) inputImage.getWidth();
        double heightProportion = (double) height / (double) inputImage.getHeight();

        // Лучше работать по большой, поэтому ищем её

        if (newAspectRatio || params.isCutImage())
            finalProportion = Math.max(widthProportion, heightProportion);
        else
            finalProportion = Math.min(widthProportion, heightProportion);


        ImageData resizeResult = executor.execute(proportionResizeCase, inputImage, new ProportionResizeCase.Input(finalProportion));

        // Если соотношение сторон одинаковое, то не имеет смысла создавать новое изображение и в него вставлять результат, вернём как есть
        if (!newAspectRatio)
            return resizeResult;

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        BufferedImage buffImage = ImageIOUtils.loadImage(resizeResult);

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
