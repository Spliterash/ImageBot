package ru.spliterash.imageBot.domain.exceptions;

public class CaseErrorException extends ImageBotDomainException {
    public CaseErrorException(String message) {
        super(ImageErrorReasons.CASE_ERROR, message);
    }
}
