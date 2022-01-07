package ru.spliterash.imageBot.messengers.domain.attachment.income;

public interface IncomeAttachment {
    BaseIncomeAttachment.Type getType();

    String getId();
}
