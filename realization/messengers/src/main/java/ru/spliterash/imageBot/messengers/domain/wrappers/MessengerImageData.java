package ru.spliterash.imageBot.messengers.domain.wrappers;

import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.messengers.domain.attachment.income.KnownIncomeImageAttachment;
import ru.spliterash.imageBot.messengers.domain.exceptions.MessengerException;
import ru.spliterash.imageBot.messengers.domain.port.URLDownloader;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public class MessengerImageData implements ImageData {
    private final URLDownloader client;
    private final KnownIncomeImageAttachment attachment;

    @Override
    public InputStream read() {
        try {
            return client.load(attachment.getUrl());
        } catch (IOException e) {
            throw new MessengerException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public int getWidth() {
        return attachment.getWidth();
    }

    @Override
    public int getHeight() {
        return attachment.getHeight();
    }
}
