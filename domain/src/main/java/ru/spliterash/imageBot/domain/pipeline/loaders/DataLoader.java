package ru.spliterash.imageBot.domain.pipeline.loaders;

import ru.spliterash.imageBot.domain.entities.Data;

import java.io.IOException;
import java.io.InputStream;

public interface DataLoader<D extends Data> extends Data {
    D load() throws IOException;

    @Override
    default InputStream read() {
        throw new UnsupportedOperationException("it a loader. Case marked as NoReadCase, but data load init.");
    }
}
