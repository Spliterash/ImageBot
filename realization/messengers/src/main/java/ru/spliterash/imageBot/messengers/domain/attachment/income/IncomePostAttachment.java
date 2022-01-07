package ru.spliterash.imageBot.messengers.domain.attachment.income;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class IncomePostAttachment extends BaseIncomeAttachment {
    /**
     * Содержимое поста
     */
    private final String text;
    /**
     * Прилагаемое к посту
     */
    private final List<IncomeAttachment> attachments;

    @Override
    public Type getType() {
        return Type.POST;
    }
}
