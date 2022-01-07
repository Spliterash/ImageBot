package ru.spliterash.imageBot.messengers.domain;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import ru.spliterash.imageBot.domain.def.CaseIO;
import ru.spliterash.imageBot.domain.def.bean.Bean;
import ru.spliterash.imageBot.domain.entities.Data;
import ru.spliterash.imageBot.domain.entities.TextData;
import ru.spliterash.imageBot.domain.pipeline.PipelineService;
import ru.spliterash.imageBot.domain.pipeline.PipelineStep;
import ru.spliterash.imageBot.messengers.domain.attachment.income.IncomeAttachment;
import ru.spliterash.imageBot.messengers.domain.attachment.income.IncomeImageAttachment;
import ru.spliterash.imageBot.messengers.domain.attachment.income.IncomePostAttachment;
import ru.spliterash.imageBot.messengers.domain.attachment.income.KnownIncomeImageAttachment;
import ru.spliterash.imageBot.messengers.domain.message.income.IncomeMessage;
import ru.spliterash.imageBot.messengers.domain.message.outcome.OutcomeMessage;
import ru.spliterash.imageBot.messengers.domain.port.URLDownloader;
import ru.spliterash.imageBot.messengers.domain.wrappers.MessengerImageData;
import ru.spliterash.imageBot.messengers.domain.wrappers.MessengerUnknownImageData;
import ru.spliterash.imageBot.pipelines.text.TextPipelineGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractMessenger implements Bean {
    private final TextPipelineGenerator generator;
    private final URLDownloader urlDownloader;
    private final PipelineService pipelineService;

    /**
     * Присылать сюда только те сообщения, которые нужно обработать и содержащие только текст обработки
     */
    public void notifyMessage(IncomeMessage message) {
        String text = message.getText();

        CaseIO transfer = transfer(message);
        List<PipelineStep<?, ?>> list = generator.parse(text);
        CaseIO result = pipelineService.run(list, transfer);

        OutcomeMessage outcome = OutcomeMessage.builder()
                .attachments(new CaseIO(result.getValues()))
                .peerId(message.getPeerId())
                .replyTo(message.getId())
                .text("Результат работы pipeline'а")
                .build();

        sendMessage(outcome);
    }

    private CaseIO transfer(IncomeMessage message) {
        List<Data> dataList = new ArrayList<>();

        for (IncomeAttachment attachment : message.getAttachments()) {
            dataList.addAll(parseAttachment(attachment));
        }

        return new CaseIO(dataList);
    }

    private Collection<Data> parseAttachment(IncomeAttachment attachment) {
        List<Data> data = new ArrayList<>(1);

        if (attachment instanceof IncomeImageAttachment) {
            if (attachment instanceof KnownIncomeImageAttachment)
                data.add(new MessengerImageData(urlDownloader, (KnownIncomeImageAttachment) attachment));
            else
                data.add(new MessengerUnknownImageData(urlDownloader, (IncomeImageAttachment) attachment));
        } else if (attachment instanceof IncomePostAttachment)
            data.addAll(parsePost((IncomePostAttachment) attachment));

        return data;
    }

    private Collection<Data> parsePost(IncomePostAttachment post) {
        List<Data> data = new ArrayList<>(post.getAttachments().size());
        if (StringUtils.isNotEmpty(post.getText()))
            data.add(new TextData(post.getText()));
        for (IncomeAttachment attachment : post.getAttachments()) {
            data.addAll(parseAttachment(attachment));
        }

        return data;
    }

    public abstract void sendMessage(OutcomeMessage message);
}
