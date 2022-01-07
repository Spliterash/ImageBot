package ru.spliterash.imageBot.domain.pipeline.loader.dataDownloader.wrappers;

import ru.spliterash.imageBot.domain.entities.ImageData;

import java.io.InputStream;

/**
 * Лютый костыль, чтобы не загружать картинки в delete кейсе
 */
public class ImageDataLoaderWrapper implements ImageData {
    @Override
    public InputStream read() {
        throw new UnsupportedOperationException("Sry but no");
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }
}
