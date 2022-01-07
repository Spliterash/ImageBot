package ru.spliterash.imageBot.domain.exceptions;

import java.io.IOException;

public class ImageReadError extends ImageBotDomainException {
    public ImageReadError(IOException e) {
        super(e.getLocalizedMessage(), e);
        placeholders.put("error", e.getLocalizedMessage());
    }
}
