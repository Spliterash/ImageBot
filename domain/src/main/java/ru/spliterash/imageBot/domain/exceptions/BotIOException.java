package ru.spliterash.imageBot.domain.exceptions;

import java.io.IOException;

/**
 * IO ошибка
 */
public class BotIOException extends ImageBotBaseException {
    public BotIOException(IOException cause) {
        super("IO exception", cause);
    }
}
