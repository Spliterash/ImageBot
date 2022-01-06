package ru.spliterash.imageBot.pipelines.text.exception;

import ru.spliterash.imageBot.domain.exceptions.ImageBotDomainException;
import ru.spliterash.imageBot.domain.exceptions.ImageErrorReasons;

public class CommandNotFound extends ImageBotDomainException {
    public CommandNotFound(String message) {
        super(ImageErrorReasons.INPUT, message);
    }
}
