package ru.spliterash.imageBot.pipelines.text.exception;

import ru.spliterash.imageBot.domain.exceptions.ImageBotDomainException;

public class CommandNotFound extends ImageBotDomainException {
    public CommandNotFound(String message) {
        super(message);
    }
}
