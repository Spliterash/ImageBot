package ru.spliterash.imageBot.realization.image.imageIO.cases.glue;

import ru.spliterash.imageBot.domain.cases.CoverImageUseCase;
import ru.spliterash.imageBot.domain.cases.ComicImageCase;
import ru.spliterash.imageBot.domain.cases.ProportionResizeCase;
import ru.spliterash.imageBot.domain.def.CaseExecutor;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.domain.exceptions.CaseErrorException;
import ru.spliterash.imageBot.realization.image.imageIO.entities.ImageIOImageData;
import ru.spliterash.imageBot.realization.image.imageIO.utils.ImageIOUtils;
import ru.spliterash.imageBot.realization.image.utils.obj.Coords;
import ru.spliterash.imageBot.realization.image.utils.obj.RectangleCoords;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ComicImageCaseIn {

    private final ComicImageCase.Params input;
    private final CaseExecutor executor;
    private final ProportionResizeCase proportionResizeCase;
    private final CoverImageUseCase coverImageUseCase;

    private final int rowsCount;
    private final int columnCount;
    private final List<ImageData> inputImages;
    private final int lineSize;

    private int paneX;
    private int paneY;
    private int sizeX;
    private int sizeY;
    private double proportion;

    public ComicImageCaseIn(List<ImageData> inputImages, ComicImageCase.Params input, CaseExecutor executor, ProportionResizeCase proportionResizeCase, CoverImageUseCase coverImageUseCase) {
        this.input = input;
        this.executor = executor;
        this.proportionResizeCase = proportionResizeCase;
        this.inputImages = inputImages;
        this.coverImageUseCase = coverImageUseCase;
        int totalImages = this.inputImages.size();

        Integer rows = input.getRows();
        Integer columns = input.getColumns();

        if (rows == null && columns == null)
            columns = 2;

        if (columns != null) {
            columnCount = columns;
            rowsCount = (int) Math.ceil((double) totalImages / (double) columnCount);
        } else {
            rowsCount = rows;
            columnCount = (int) Math.ceil((double) totalImages / (double) rowsCount);
        }

        lineSize = input.isNeedBorder() ? 2 : 0;
    }

    public ImageData execute() {
        switch (input.getResizeMode()) {
            case NONE: {
                // ???????? ?????????? ?????????????? ????????????????
                paneX = inputImages
                        .stream()
                        .mapToInt(ImageData::getWidth)
                        .max()
                        .orElse(128);

                paneY = inputImages
                        .stream()
                        .mapToInt(ImageData::getHeight)
                        .max()
                        .orElse(128);
                break;
            }
            case COVER: {
                // ???????? ?????????????? ???????????? ????????????????
                paneX = (int) Math.floor(inputImages
                        .stream()
                        .mapToInt(ImageData::getWidth)
                        .average()
                        .orElseThrow());
                paneY = (int) Math.floor(inputImages
                        .stream()
                        .mapToInt(ImageData::getHeight)
                        .average()
                        .orElseThrow());
                break;
            }
            default:
                throw new CaseErrorException("?????? ???????????? ?????????????????? ???? ?????????? ??????????????????");

        }

        sizeX = columnCount * paneX;
        sizeY = rowsCount * paneY;

        if (input.isNeedBorder()) {
            sizeX += lineSize * (columnCount - 1);
            sizeY += lineSize * (rowsCount - 1);
        }

        int max;
        if (input.getMax() < 256)
            max = 256;
        else if (input.getMax() > 6144)
            max = 6144;
        else
            max = input.getMax();

        if (sizeX > sizeY && sizeX > max)
            proportion = (double) max / (double) sizeX;
        else if (sizeY > sizeX && sizeY > max)
            proportion = (double) max / (double) sizeY;
        else
            proportion = 1;

        // ?????????????? ?????? ?????????????? ?? ?????????????????????? ????????????????????????

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
                ImageData image = inputImages.get(imageCounter);
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
                        throw new CaseErrorException("???? ???? ???????????? ???? ?????????? ?????????????????? ???? ??????????");
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

        // ???????????????? ???????????? ???????????????????? ??????????

        if (input.isNeedBorder()) {
            for (int column = 1; column < columnCount; column++) {
                int cord = getLineCord(paneX, column);

                Coords start = new Coords(cord, 0);
                Coords end = new Coords(cord + (lineSize - 1), sizeY - 1);

                RectangleCoords rectangle = new RectangleCoords(start, end);
                Graphics2D lineGraph = img.createGraphics();
                lineGraph.setColor(Color.BLACK);

                ImageIOUtils.draw(lineGraph, rectangle);
            }

            for (int row = 1; row < rowsCount; row++) {
                int cord = getLineCord(paneY, row);

                Coords start = new Coords(0, cord);
                Coords end = new Coords(sizeX - 1, cord + (lineSize - 1));

                RectangleCoords rectangle = new RectangleCoords(start, end);
                Graphics2D lineGraph = img.createGraphics();
                lineGraph.setColor(Color.BLACK);

                ImageIOUtils.draw(lineGraph, rectangle);
            }
        }

        return new ImageIOImageData(img);
    }

    private BufferedImage simpleResize(ImageData image) {
        BufferedImage bufferedImage = ImageIOUtils.loadImage(image);
        if (proportion == 1)
            return bufferedImage;
        else
            return ImageIOUtils.loadImage(executor.execute(
                    proportionResizeCase,
                    image,
                    new ProportionResizeCase.Input(proportion)
            ));
    }

    private BufferedImage coverResize(ImageData image, int width, int height) {
        return ImageIOUtils.loadImage(executor.execute(
                coverImageUseCase,
                image,
                CoverImageUseCase.Input.builder()
                        .width(width)
                        .height(height)
                        .cutImage(true)
                        .build())
        );
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
