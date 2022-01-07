package ru.spliterash.imageBot.messengers.domain.attachment.income;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UnknownIncomeImageAttachment extends BaseIncomeAttachment implements IncomeImageAttachment {
    private final String url;

    @Override
    public Type getType() {
        return Type.IMAGE;
    }
}
