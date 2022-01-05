package ru.spliterash.imageBot.realization.image.imageIO.in;

import ru.spliterash.imageBot.domain.cases.CoverImageUseCase;
import ru.spliterash.imageBot.domain.cases.GlueImagesCase;
import ru.spliterash.imageBot.domain.cases.ResizeCase;
import ru.spliterash.imageBot.domain.def.CaseIO;
import ru.spliterash.imageBot.domain.entities.DomainImage;
import ru.spliterash.imageBot.domain.exceptions.CaseErrorException;
import ru.spliterash.imageBot.realization.image.imageIO.entities.ImageIOImage;
import ru.spliterash.imageBot.realization.image.imageIO.utils.ImageIOUtils;
import ru.spliterash.imageBot.realization.image.utils.obj.Coords;
import ru.spliterash.imageBot.realization.image.utils.obj.RectangleCoords;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ImageIOGlueImageCaseIn {

    private final GlueImagesCase.GlueImagesInput input;
    private final ResizeCase resizeCase;
    private final CoverImageUseCase coverImageUseCase;

    private final int rowsCount;
    private final int columnCount;
    private final List<DomainImage> inputImages;
    private final int lineSize;

    private int paneX;
    private int paneY;
    private int sizeX;
    private int sizeY;
    private double proportion;

    public ImageIOGlueImageCaseIn(CaseIO io, GlueImagesCase.GlueImagesInput input, ResizeCase resizeCase, CoverImageUseCase coverImageUseCase) {
        this.input = input;
        this.resizeCase = resizeCase;
        inputImages = io.images();
        this.coverImageUseCase = coverImageUseCase;
        int totalImages = inputImages.size();
        rowsCount = (int) Math.ceil((double) totalImages / (double) input.getColumns());
        columnCount = input.getColumns();

        lineSize = input.isNeedBorder() ? 2 : 0;
    }

    public DomainImage execute() {
        switch (input.getResizeMode()) {
            case NONE: {
                // Ищем самую большую картинку
                paneX = inputImages
                        .stream()
                        .mapToInt(DomainImage::getWidth)
                        .max()
                        .orElse(128);

                paneY = inputImages
                        .stream()
                        .mapToInt(DomainImage::getHeight)
                        .max()
                        .orElse(128);
                break;
            }
            case COVER: {
                // Ищем средний размер картинок
                paneX = (int) Math.floor(inputImages
                        .stream()
                        .mapToInt(DomainImage::getWidth)
                        .average()
                        .orElseThrow());
                paneY = (int) Math.floor(inputImages
                        .stream()
                        .mapToInt(DomainImage::getHeight)
                        .average()
                        .orElseThrow());
                break;
            }
            default:
                throw new CaseErrorException("Эта ошибка физически не может произойти");

        }

        sizeX = columnCount * paneX;
        sizeY = rowsCount * paneY;

        if (input.isNeedBorder()) {
            sizeX += lineSize * (columnCount - 1);
            sizeY += lineSize * (rowsCount - 1);
        }

        if (sizeX > sizeY && sizeX > input.getMax())
            proportion = (double) input.getMax() / (double) sizeX;
        else if (sizeY > sizeX && sizeY > input.getMax())
            proportion = (double) input.getMax() / (double) sizeY;
        else
            proportion = 1;

        // Срубаем все размеры к максимально разрешенному

        sizeX *= proportion;
        sizeY *= proportion;

        paneX *= proportion;
        paneY *= proportion;


        BufferedImage img = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = img.createGraphics();

        g.setPaint(Color.WHITE);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());

        g.dispose();

        for (int rowCounter = 0, imageCounter = 0; rowCounter < rowsCount; rowCounter++) {
            for (int columnCounter = 0; columnCounter < columnCount && imageCounter < inputImages.size(); columnCounter++, imageCounter++) {
                BufferedImage buffImage;
                DomainImage image = inputImages.get(imageCounter);
                RectangleCoords c = getFrameCoords(rowCounter, columnCounter);

                if (input.getPadding() > 0)
                    c = c.expand(-input.getPadding());

                switch (input.getResizeMode()) {
                    case COVER:
                        buffImage = coverResize(image, c.getWidth(), c.getHeight());
                        break;
                    case NONE:
                        buffImage = simpleResize(image);
                        break;
                    default:
                        throw new CaseErrorException("Ну эт ошибка ну никак дропнутся не может");
                }


                Coords startFrom;
                if (buffImage.getWidth() == c.getWidth() && buffImage.getHeight() == c.getHeight())
                    startFrom = c.getA();
                else
                    startFrom = c.center().add(
                            -(buffImage.getWidth() / 2),
                            -(buffImage.getHeight() / 2)
                    );

                g = img.createGraphics();
                g.drawImage(buffImage, startFrom.getX(), startFrom.getY(), buffImage.getWidth(), buffImage.getHeight(), null);
                g.dispose();
            }
        }

        // Осталось только нарисовать линию

        if (input.isNeedBorder()) {
            for (int column = 1; column < columnCount; column++) {
                int cord = getLineCord(paneY, column);

                Coords start = new Coords(0, cord);
                Coords end = new Coords(sizeX - 1, cord + (lineSize - 1));

                RectangleCoords rectangle = new RectangleCoords(start, end);
                Graphics2D lineGraph = img.createGraphics();
                lineGraph.setColor(Color.BLACK);

                ImageIOUtils.draw(lineGraph, rectangle);
            }

            for (int row = 1; row < rowsCount; row++) {
                int cord = getLineCord(paneX, row);

                Coords start = new Coords(cord, 0);
                Coords end = new Coords(cord + (lineSize - 1), sizeY - 1);

                RectangleCoords rectangle = new RectangleCoords(start, end);
                Graphics2D lineGraph = img.createGraphics();
                lineGraph.setColor(Color.BLACK);

                ImageIOUtils.draw(lineGraph, rectangle);
            }
        }

        return new ImageIOImage(img);
    }

    private BufferedImage simpleResize(DomainImage image) {
        BufferedImage bufferedImage = ImageIOUtils.loadImage(image);
        if (proportion == 1)
            return bufferedImage;
        else
            return ImageIOUtils.loadImage(resizeCase.execute(
                            image,
                            new ResizeCase.Input(proportion)
                    )
                    .firstImage());
    }

    private BufferedImage coverResize(DomainImage image, int width, int height) {
        return ImageIOUtils.loadImage(coverImageUseCase.execute(image, CoverImageUseCase.Input.builder()
                        .width(width)
                        .height(height)
                        .cutImage(true)
                        .build())
                .firstImage());
    }

    private RectangleCoords getFrameCoords(int row, int column) {

        int xOffset = lineSize * column;
        int yOffset = lineSize * row;


        int x1 = (column * paneX) + xOffset;
        int y1 = (row * paneY) + yOffset;


        int x2 = (((column + 1) * paneX) - 1) + xOffset;
        int y2 = (((row + 1) * paneY) - 1) + yOffset;

        return new RectangleCoords(
                new Coords(x1, y1),
                new Coords(x2, y2)
        );
    }

    private int getLineCord(int paneSize, int i) {
        int offset = lineSize * (i - 1);

        return (paneSize * i) + (offset);
    }
}
