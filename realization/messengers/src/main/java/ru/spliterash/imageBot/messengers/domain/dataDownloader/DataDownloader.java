package ru.spliterash.imageBot.messengers.domain.dataDownloader;

import ru.spliterash.imageBot.domain.entities.Data;

import java.io.IOException;

public interface DataDownloader<D extends Data> {
    D load() throws IOException;
}
