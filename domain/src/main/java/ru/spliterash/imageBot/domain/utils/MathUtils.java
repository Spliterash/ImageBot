package ru.spliterash.imageBot.domain.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MathUtils {
    public boolean equalsDouble(double d1, double d2) {
        return Math.abs(d1 - d2) < 1E-7;
    }
}
