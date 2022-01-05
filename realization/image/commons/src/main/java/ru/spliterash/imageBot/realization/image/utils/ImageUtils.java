package ru.spliterash.imageBot.realization.image.utils;

import java.awt.*;

public class ImageUtils {
    public static Color random(Color c1, Color c2) {
        double r = Math.random();
        double rInverse = 1.0 - r;

        return new Color(
                (int) (r * c1.getRed() + rInverse * c2.getRed()),
                (int) (r * c1.getGreen() + rInverse * c2.getGreen()),
                (int) (r * c1.getBlue() + rInverse * c2.getBlue())
        );
    }
}
