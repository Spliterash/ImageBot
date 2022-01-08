package ru.spliterash.imageBot.domain.exceptions;

/**
 * IO ошибка
 */
public class BotExceptionWrapper extends ImageBotBaseException {
    public BotExceptionWrapper(Exception cause) {
        super("Not runtime exception", cause);
    }
}
