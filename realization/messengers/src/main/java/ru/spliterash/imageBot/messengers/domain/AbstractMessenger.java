package ru.spliterash.imageBot.messengers.domain;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import ru.spliterash.imageBot.domain.def.CaseIO;
import ru.spliterash.imageBot.domain.def.bean.Bean;
import ru.spliterash.imageBot.domain.entities.Data;
import ru.spliterash.imageBot.domain.entities.TextData;
import ru.spliterash.imageBot.domain.pipeline.PipelineOutput;
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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;

@RequiredArgsConstructor
public abstract class AbstractMessenger implements Bean {
    protected final TextPipelineGenerator generator;
    protected final URLDownloader urlDownloader;
    protected final PipelineService pipelineService;
    protected final Executor executor;

    /**
     * Присылать сюда только те сообщения, которые нужно обработать и содержащие только текст обработки
     */
    protected void notifyMessage(IncomeMessage message) {
        String text = message.getText();

        CaseIO transfer = transfer(message);
        long start = System.currentTimeMillis();
        List<PipelineStep<?, ?>> list = generator.parse(text);
        long parseEnd = System.currentTimeMillis() - start;
        start = System.currentTimeMillis();
        PipelineOutput result = pipelineService.run(list, transfer);
        long pipeEnd = System.currentTimeMillis() - start;

        StringBuilder builder = new StringBuilder();
        for (PipelineOutput.OperationCost cost : result.getCost()) {
            builder.append(cost.getOperationName()).append(": ").append(String.format("%.3f", (cost.getCost() / 1000D))).append("\n");
        }

        OutcomeMessage outcome = OutcomeMessage.builder()
                .attachments(result.getOutput())
                .peerId(message.getPeerId())
                .replyTo(message.getId())
                .text(MessageFormat.format(
                        "Готово. Время парсинга: {0}\nВремя всех пайплайнов: {1}\n{2}",
                        String.format("%.3f", (parseEnd / 1000D)),
                        String.format("%.3f", (pipeEnd / 1000D)),
                        builder.toString())
                )
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
