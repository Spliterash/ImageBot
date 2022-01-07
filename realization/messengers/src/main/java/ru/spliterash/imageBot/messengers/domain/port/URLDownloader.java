package ru.spliterash.imageBot.messengers.domain.port;

import java.io.IOException;
import java.io.InputStream;

public interface URLDownloader {
    InputStream load(String url) throws IOException;
}
