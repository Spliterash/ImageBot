package ru.spliterash.imageBot.domain.def.cases.markers;

/**
 * Этот кейс нельзя выполнить если размер изображения больше определённого
 */
public interface MaxLimitCase {
    default int getMaxSquare() {
        return 512 * 512;
    }

}
