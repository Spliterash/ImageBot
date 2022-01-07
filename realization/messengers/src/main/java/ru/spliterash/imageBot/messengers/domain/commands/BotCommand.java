package ru.spliterash.imageBot.messengers.domain.commands;

import ru.spliterash.imageBot.domain.def.bean.Bean;
import ru.spliterash.imageBot.messengers.domain.AbstractMessenger;
import ru.spliterash.imageBot.messengers.domain.message.income.IncomeMessage;

import java.util.List;

public interface BotCommand extends Bean {
    List<String> getAliases();

    String help();

    /**
     * @param message Полный объект сообщения
     * @param args    Аргументы после команды
     */
    void execute(AbstractMessenger messenger, IncomeMessage message, String[] args, String[] anotherLines);

    /**
     * Сделать что-то с эксепшеном, который возник в ходе обработки команды
     */
    default void handleException(Exception exception, AbstractMessenger messenger, String peerId) {
        messenger.sendMessage(peerId, exception.getMessage());
    }

    default String aliasLine() {
        return String.join(",", getAliases());
    }
}
