package ru.spliterash.imageBot.domain.exceptions;

import java.io.IOException;

public class BotIOException extends ImageBotDomainException {
    public BotIOException(IOException cause) {
        super("IO exception", cause);
    }
}
