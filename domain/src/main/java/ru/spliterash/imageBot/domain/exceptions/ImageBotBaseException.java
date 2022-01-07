package ru.spliterash.imageBot.domain.exceptions;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ImageBotBaseException extends RuntimeException {
    protected final Map<String, String> placeholders = new HashMap<>();

    public ImageBotBaseException(String message) {
        super(message);
    }

    public ImageBotBaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
