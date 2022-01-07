package ru.spliterash.imageBot.pipelines.text.exception;

import ru.spliterash.imageBot.domain.exceptions.ImageBotBaseException;

public class CommandParseException extends ImageBotBaseException {
    public CommandParseException(String message) {
        super(message);
    }

    public CommandParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
