package ru.spliterash.imageBot.messengers.domain.exceptions;

import ru.spliterash.imageBot.domain.exceptions.ImageBotBaseException;

public class MessengerUserNotFound extends ImageBotBaseException {
    public MessengerUserNotFound(String id) {
        super("User with id " + id + " not found");
    }
}
