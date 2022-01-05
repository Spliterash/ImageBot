package ru.spliterash.imageBot.domain.exceptions;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ImageBotDomainException extends RuntimeException {
    private final ImageErrorReasons reason;
    protected final Map<String, String> placeholders = new HashMap<>();

    public ImageBotDomainException(ImageErrorReasons reason, String message) {
        super(message);
        this.reason = reason;
    }

    public ImageBotDomainException(ImageErrorReasons reason, String message, Throwable cause) {
        super(message, cause);
        this.reason = reason;
    }
}
