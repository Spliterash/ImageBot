package ru.spliterash.imageBot.domain.entities;

import java.io.InputStream;

/**
 * Контейнер для хранения изображений
 */
public interface DomainImage {
    InputStream read();

    int getWidth();

    int getHeight();
}
