package ru.spliterash.imageBot.domain.pipeline.loaders;

import ru.spliterash.imageBot.domain.entities.ImageData;

public interface ImageLoader extends DataLoader<ImageData>, ImageData {
    @Override
    default int getWidth() {
        throw new UnsupportedOperationException();
    }

    default int getHeight() {
        throw new UnsupportedOperationException();
    }
}
