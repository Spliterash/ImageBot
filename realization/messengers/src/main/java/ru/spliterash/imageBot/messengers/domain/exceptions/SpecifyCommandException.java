package ru.spliterash.imageBot.messengers.domain.exceptions;

import ru.spliterash.imageBot.domain.exceptions.ImageBotBaseException;

public class SpecifyCommandException extends ImageBotBaseException {
    public SpecifyCommandException() {
        super("Команда не выбрана.\"зов помощь\" для помощи");
    }
}
