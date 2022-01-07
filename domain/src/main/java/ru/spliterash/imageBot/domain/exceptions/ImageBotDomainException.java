package ru.spliterash.imageBot.domain.exceptions;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ImageBotDomainException extends RuntimeException {
    protected final Map<String, String> placeholders = new HashMap<>();

    public ImageBotDomainException(String message) {
        super(message);
    }

    public ImageBotDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
