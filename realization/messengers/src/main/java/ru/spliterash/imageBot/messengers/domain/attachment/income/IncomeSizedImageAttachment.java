package ru.spliterash.imageBot.messengers.domain.attachment.income;

public interface IncomeSizedImageAttachment extends IncomeImageAttachment {

    int getWidth();

    int getHeight();
}
