package ru.spliterash.imageBot.pipelines.text.exception;

import ru.spliterash.imageBot.domain.exceptions.ImageBotDomainException;

public class CommandParseException extends ImageBotDomainException {
    public CommandParseException(String message) {
        super(message);
    }

    public CommandParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
