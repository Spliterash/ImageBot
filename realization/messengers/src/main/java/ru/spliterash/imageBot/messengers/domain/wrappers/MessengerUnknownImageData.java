package ru.spliterash.imageBot.messengers.domain.wrappers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.domain.utils.ImageUtils;
import ru.spliterash.imageBot.messengers.domain.attachment.income.IncomeImageAttachment;
import ru.spliterash.imageBot.messengers.domain.port.URLDownloader;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public class MessengerUnknownImageData implements ImageData {
    private final URLDownloader client;
    private final IncomeImageAttachment attachment;

    private byte[] body;
    @Getter
    private transient int width = -1;
    @Getter
    private transient int height = -1;

    @Override
    public InputStream read() {
        return new ByteArrayInputStream(body);
    }


    private void bodyDownload() {
        if (body == null) {
            try {
                body = IOUtils.toByteArray(client.load(attachment.getUrl()));

                Dimension img = ImageUtils.getImageDimension(new ByteArrayInputStream(body));

                width = img.width;
                height = img.height;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
