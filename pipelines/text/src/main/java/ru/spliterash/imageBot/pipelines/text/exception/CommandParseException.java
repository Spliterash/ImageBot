package ru.spliterash.imageBot.pipelines.text.exception;

import ru.spliterash.imageBot.domain.exceptions.ImageBotDomainException;
import ru.spliterash.imageBot.domain.exceptions.ImageErrorReasons;

public class CommandParseException extends ImageBotDomainException {
    public CommandParseException(String message) {
        super(ImageErrorReasons.INPUT, message);
    }

    public CommandParseException(String message, Throwable cause) {
        super(ImageErrorReasons.INPUT, message, cause);
    }
}
