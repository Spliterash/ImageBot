package ru.spliterash.imageBot.messengers.domain.listener;

import ru.spliterash.imageBot.messengers.domain.message.income.IncomeMessage;

public interface MessageListener {
    void accept(IncomeMessage message);
}
