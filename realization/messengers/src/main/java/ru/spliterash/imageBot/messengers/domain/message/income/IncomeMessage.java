package ru.spliterash.imageBot.messengers.domain.message.income;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.spliterash.imageBot.messengers.domain.attachment.income.IncomeAttachment;

import java.util.List;

@Getter
@SuperBuilder
public class IncomeMessage {
    private final String id;
    private final String text;
    /**
     * Место назначения сообщения
     */
    private final String peerId;

    private final List<IncomeMessage> reply;
    private final List<IncomeAttachment> attachments;


    private final Sender sender;

    public interface Sender {
        String getId();

        String getName();
    }
}
