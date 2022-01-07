package ru.spliterash.imageBot.messengers.domain.attachment.income;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class BaseIncomeAttachment implements IncomeAttachment {
    private final String id;

    public enum Type {
        IMAGE, VIDEO, POST
    }
}
