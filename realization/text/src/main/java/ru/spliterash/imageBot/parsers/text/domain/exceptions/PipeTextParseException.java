package ru.spliterash.imageBot.parsers.text.domain.exceptions;

import ru.spliterash.imageBot.domain.exceptions.ImageBotBaseException;

public class PipeTextParseException extends ImageBotBaseException {
    public PipeTextParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public PipeTextParseException(String message) {
        super(message);
    }
}
