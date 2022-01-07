package ru.spliterash.imageBot.messengers.domain.dataDownloader.types;

import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.entities.Data;
import ru.spliterash.imageBot.messengers.domain.dataDownloader.DataDownloader;

@RequiredArgsConstructor
public class Empty<D extends Data> implements DataDownloader<D> {
    private final D data;

    @Override
    public D load() {
        return data;
    }
}
