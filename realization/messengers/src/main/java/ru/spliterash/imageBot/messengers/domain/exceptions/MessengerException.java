package ru.spliterash.imageBot.messengers.domain.exceptions;

import ru.spliterash.imageBot.domain.exceptions.ImageBotDomainException;

public class MessengerException extends ImageBotDomainException {
    public MessengerException(String message) {
        super(message);
    }

    public MessengerException(String message, Throwable cause) {
        super(message, cause);
    }
}
