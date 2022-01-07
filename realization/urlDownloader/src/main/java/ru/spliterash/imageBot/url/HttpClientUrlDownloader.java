package ru.spliterash.imageBot.url;

import lombok.RequiredArgsConstructor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import ru.spliterash.imageBot.domain.def.bean.Bean;
import ru.spliterash.imageBot.messengers.domain.port.URLDownloader;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public class HttpClientUrlDownloader implements URLDownloader, Bean {
    private final HttpClient httpClient = HttpClients.createDefault();

    @Override
    public InputStream load(String url) throws IOException {
        return httpClient.execute(new HttpGet(url)).getEntity().getContent();
    }
}
