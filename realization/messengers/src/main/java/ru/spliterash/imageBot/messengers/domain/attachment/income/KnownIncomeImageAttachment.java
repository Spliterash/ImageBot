package ru.spliterash.imageBot.messengers.domain.attachment.income;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class KnownIncomeImageAttachment extends BaseIncomeAttachment implements IncomeSizedImageAttachment {
    private final String url;
    private final int width;
    private final int height;


    @Override
    public Type getType() {
        return Type.IMAGE;
    }
}
