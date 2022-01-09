package ru.spliterash.imageBot.realization.image.utils.obj;

import lombok.Value;
import ru.spliterash.imageBot.realization.image.utils.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Содержит минимальную и максимальную точку, а так же методы для получения углов
 * <pre>
 * {@code
 *
 * (min)A----D
 *      |    |
 *      B----C(max)
 * }
 * </pre>
 */
@Value
public class RectangleCoords {
    Coords minPoint;
    Coords maxPoint;


    public RectangleCoords(
            Coords p1,
            Coords p2
    ) {
        this.minPoint = new Coords(
                Math.min(p1.getX(), p2.getX()),
                Math.min(p1.getY(), p2.getY())
        );
        this.maxPoint = new Coords(
                Math.max(p1.getX(), p2.getX()),
                Math.max(p1.getY(), p2.getY())
        );
    }

    public RectangleCoords expand(int radius) {
        return new RectangleCoords(
                minPoint.add(-radius, -radius),
                maxPoint.add(radius, radius)
        );
    }

    public Coords getA() {
        return minPoint;
    }

    public Coords getB() {
        return new Coords(minPoint.getX(), maxPoint.getY());
    }

    public Coords getC() {
        return maxPoint;
    }

    public Coords getD() {
        return new Coords(maxPoint.getX(), minPoint.getY());
    }

    public int getWidth() {
        return (maxPoint.getX() - minPoint.getX()) + 1;
    }

    public int getHeight() {
        return (maxPoint.getY() - minPoint.getY()) + 1;
    }

    public Coords center() {
        return new Coords(
                minPoint.getX() + (maxPoint.getX() - minPoint.getX()) / 2,
                minPoint.getY() + (maxPoint.getY() - minPoint.getY()) / 2
        );
    }

    /**
     * Для дебага
     */
    public void draw(BufferedImage image) {
        Coords a = getA();
        Coords b = getB();
        Coords c = getC();
        Coords d = getD();

        Graphics2D g = image.createGraphics();
        g.setColor(ImageUtils.random(Color.DARK_GRAY, Color.BLACK));

        g.fillRect(a.getX(), a.getY(), getWidth(), getHeight());

        g.setPaint(ImageUtils.random(Color.CYAN,Color.yellow));

        g.drawLine(a.getX(), a.getY(), b.getX(), b.getY());
        g.drawLine(b.getX(), b.getY(), c.getX(), c.getY());
        g.drawLine(c.getX(), c.getY(), d.getX(), d.getY());
        g.drawLine(d.getX(), d.getY(), a.getX(), a.getY());


        g.drawLine(a.getX(), a.getY(), c.getX(), c.getY());
        g.drawLine(d.getX(), d.getY(), b.getX(), b.getY());

        g.dispose();
    }
}
