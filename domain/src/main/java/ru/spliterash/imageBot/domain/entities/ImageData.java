package ru.spliterash.imageBot.domain.entities;

/**
 * Контейнер для хранения изображений
 */
public interface ImageData extends Data {
    int getWidth();

    int getHeight();
}
