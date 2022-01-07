package ru.spliterash.imageBot.messengers.domain.exceptions;

import ru.spliterash.imageBot.domain.exceptions.ImageBotBaseException;

public class CommandNotFound extends ImageBotBaseException {
    public CommandNotFound(String anotherString) {
        super("Команда " + anotherString + " не найдена");
    }
}
