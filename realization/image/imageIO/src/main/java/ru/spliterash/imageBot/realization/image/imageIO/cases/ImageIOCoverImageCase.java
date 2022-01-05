package ru.spliterash.imageBot.realization.image.imageIO.cases;

import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.cases.CoverImageUseCase;
import ru.spliterash.imageBot.domain.cases.ResizeCase;
import ru.spliterash.imageBot.domain.def.CaseIO;
import ru.spliterash.imageBot.domain.entities.DomainImage;
import ru.spliterash.imageBot.domain.exceptions.ImageReadError;
import ru.spliterash.imageBot.realization.image.imageIO.entities.ImageIOImage;
import ru.spliterash.imageBot.realization.image.imageIO.utils.ImageIOUtils;
import ru.spliterash.imageBot.realization.image.utils.obj.Coords;

import java.awt.*;
import java.awt.image.BufferedImage;

@RequiredArgsConstructor
public class ImageIOCoverImageCase implements CoverImageUseCase {
    private final ResizeCase resizeCase;

    @Override
    public CaseIO execute(CaseIO io, Input params) throws ImageReadError {
        DomainImage input = io.firstImage();

        BufferedImage result = new BufferedImage(params.getWidth(), params.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // Пытаемся найти вариант в котором будет меньше всего "потерь" ну или как настройки забиты

        double finalProportion;

        double widthProportion = (double) result.getWidth() / (double) input.getWidth();
        double heightProportion = (double) result.getHeight() / (double) input.getHeight();

        // Лучше работать по большой, поэтому ищем её

        if (params.isCutImage())
            finalProportion = Math.max(widthProportion, heightProportion);
        else
            finalProportion = Math.min(widthProportion, heightProportion);


        BufferedImage image = ImageIOUtils.loadImage(resizeCase.execute(input, new ResizeCase.Input(finalProportion)).firstImage());

        Coords drawTo = new Coords(
                result.getWidth() / 2,
                result.getHeight() / 2
        ).add(-(image.getWidth() / 2), -(image.getHeight() / 2));

        Graphics2D g = result.createGraphics();

        g.drawImage(image, drawTo.getX(), drawTo.getY(), image.getWidth(), image.getHeight(), null);
        g.dispose();

        return of(new ImageIOImage(result));
    }

}
