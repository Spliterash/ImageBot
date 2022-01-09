package ru.spliterash.imageBot.realization.image.imageIO.cases.line;

import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.cases.CoverImageUseCase;
import ru.spliterash.imageBot.domain.cases.LineImagesCase;
import ru.spliterash.imageBot.domain.def.CaseExecutor;
import ru.spliterash.imageBot.domain.entities.Direction;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.realization.image.imageIO.entities.ImageIOImageData;
import ru.spliterash.imageBot.realization.image.imageIO.utils.ImageIOUtils;
import ru.spliterash.imageBot.realization.image.utils.obj.Coords;
import ru.spliterash.imageBot.realization.image.utils.obj.RectangleCoords;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ImageIOLineCase extends LineImagesCase {
    private static final int BORD_SIZE = 2;

    private final CaseExecutor executor;
    private final CoverImageUseCase coverImageUseCase;

    @Override
    public ImageData process(List<ImageData> list, Params params) {
        // Максимальная длинна по какой то стороне
        // В случае если у нас горизонтальная линия, то это высота
        // В случае если у нас вертикальная линия, то это ширина
        int maxSize = list
                .stream()
                .mapToInt(i -> params.getDirection() == Direction.VERTICAL ? i.getWidth() : i.getHeight())
                .max()
                .orElseThrow();// Не может выкинуться, ибо кейс просто не может вызваться с пустой датой

        List<BufferedImage> resizedImages = new ArrayList<>();
        for (ImageData imageData : list) {
            CoverImageUseCase.Input.InputBuilder<?, ?> inputBuilder = CoverImageUseCase.Input.builder()
                    .cutImage(false);

            if (params.getDirection().equals(Direction.VERTICAL))
                inputBuilder.width(maxSize);
            else
                inputBuilder.height(maxSize);

            ImageData result = executor.execute(
                    coverImageUseCase,
                    imageData,
                    inputBuilder.build()
            );

            BufferedImage loaded = ImageIOUtils.loadImage(result);

            resizedImages.add(loaded);
        }


        int sizeAdds = params.isNeedBorder() ? (resizedImages.size() - 1) * BORD_SIZE : 0;

        int width, height;

        if (params.getDirection().equals(Direction.VERTICAL)) {
            width = maxSize;
            height = resizedImages.stream().mapToInt(BufferedImage::getHeight).sum() + sizeAdds;
        } else {
            width = resizedImages.stream().mapToInt(BufferedImage::getWidth).sum() + sizeAdds;
            height = maxSize;
        }
        BufferedImage finalResult = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = finalResult.createGraphics();

        int startAt = 0;
        for (BufferedImage image : resizedImages) {
            int x, y;
            if (params.getDirection().equals(Direction.VERTICAL)) {
                x = 0;
                y = startAt;
                startAt += image.getHeight();
            } else {
                x = startAt;
                y = 0;
                startAt += image.getWidth();
            }
            g.drawImage(image, x, y, image.getWidth(), image.getHeight(), null);

            if (params.isNeedBorder()) {
                RectangleCoords rectangleCoords;
                if (params.getDirection().equals(Direction.VERTICAL)) {
                    rectangleCoords = new RectangleCoords(
                            new Coords(0, startAt),
                            new Coords(maxSize, startAt + BORD_SIZE)
                    );
                    startAt += BORD_SIZE;
                } else {
                    rectangleCoords = new RectangleCoords(
                            new Coords(startAt, 0),
                            new Coords(startAt + BORD_SIZE, maxSize)
                    );
                    startAt += BORD_SIZE;
                }
                g.setColor(Color.BLACK);
                ImageIOUtils.draw(g, rectangleCoords);
            }
        }


        g.dispose();

        return new ImageIOImageData(finalResult);
    }
}
