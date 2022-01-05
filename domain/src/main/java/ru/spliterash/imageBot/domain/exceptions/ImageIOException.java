package ru.spliterash.imageBot.domain.exceptions;

import java.io.IOException;

public class ImageIOException extends ImageBotDomainException {
    public ImageIOException(IOException cause) {
        super(ImageErrorReasons.IO_EXCEPTION, "IO exception", cause);
    }
}
