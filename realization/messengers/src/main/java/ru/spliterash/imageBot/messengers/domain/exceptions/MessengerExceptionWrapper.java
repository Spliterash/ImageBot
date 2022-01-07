package ru.spliterash.imageBot.messengers.domain.exceptions;

import lombok.Getter;
import ru.spliterash.imageBot.domain.exceptions.ImageBotBaseException;

/**
 * Обёртка для эксепшенов, которые не Runtime
 */
@Getter
public class MessengerExceptionWrapper extends ImageBotBaseException {
    private final String peerId;

    public MessengerExceptionWrapper(String message, String peerId, Throwable cause) {
        super(message, cause);
        this.peerId = peerId;
    }
}
