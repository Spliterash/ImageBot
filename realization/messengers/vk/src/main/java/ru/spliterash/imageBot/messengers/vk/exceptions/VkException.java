package ru.spliterash.imageBot.messengers.vk.exceptions;

import ru.spliterash.imageBot.messengers.domain.exceptions.MessengerException;

public class VkException extends MessengerException {
    public VkException(Throwable cause) {
        super("Ошибка выполнения запроса VK: " + cause.getMessage(), cause);
    }

    public VkException(String message, Throwable cause) {
        super(message, cause);
    }

    public VkException(String message) {
        super(message);
    }
}
