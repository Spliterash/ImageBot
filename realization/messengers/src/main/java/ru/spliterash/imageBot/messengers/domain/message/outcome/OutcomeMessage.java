package ru.spliterash.imageBot.messengers.domain.message.outcome;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.spliterash.imageBot.domain.def.ImageCaseContext;

import java.util.Collections;

@Getter
@SuperBuilder
public class OutcomeMessage {
    private final String peerId;
    /**
     * ID сообщения на которое делается ответ
     */
    private final String replyTo;

    private final String text;
    @Builder.Default
    private final ImageCaseContext attachments = new ImageCaseContext(Collections.emptyList());
}
