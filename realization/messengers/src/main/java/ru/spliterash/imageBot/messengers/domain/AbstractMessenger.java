package ru.spliterash.imageBot.messengers.domain;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import ru.spliterash.imageBot.domain.def.bean.Bean;
import ru.spliterash.imageBot.domain.entities.Data;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.domain.entities.TextData;
import ru.spliterash.imageBot.domain.pipeline.PipelineInput;
import ru.spliterash.imageBot.domain.pipeline.PipelineOutput;
import ru.spliterash.imageBot.domain.pipeline.PipelineService;
import ru.spliterash.imageBot.domain.pipeline.PipelineStep;
import ru.spliterash.imageBot.domain.pipeline.loader.dataDownloader.loaders.ImageLoader;
import ru.spliterash.imageBot.domain.utils.ThreadUtils;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
public abstract class AbstractMessenger implements Bean {
    protected final TextPipelineGenerator generator;
    protected final URLDownloader urlDownloader;
    protected final PipelineService pipelineService;
    protected final ThreadUtils threadUtils;
    protected final ExecutorService pool = Executors.newFixedThreadPool(6);

    /**
     * Присылать сюда только те сообщения, которые нужно обработать и содержащие только текст обработки
     */
    protected void notifyMessage(IncomeMessage message) {
        String text = message.getText();

        List<Data> loaders = transfer(message);

        long start = System.currentTimeMillis();
        List<PipelineStep<?, ?>> list = generator.parse(text);
        long parseEnd = System.currentTimeMillis() - start;
        start = System.currentTimeMillis();
        PipelineOutput result = pipelineService.run(new PipelineInput(list, loaders));
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
                        "Время выполнения всех кейсов: {1}\n" +
                                "Вот они(кейсы), сверху вниз:\n" +
                                "{2}",
                        String.format("%.3f", (parseEnd / 1000D)),
                        String.format("%.3f", (pipeEnd / 1000D)),
                        builder.toString())
                )
                .build();

        sendMessage(outcome);
    }

    private List<Data> transfer(IncomeMessage message) {
        List<Data> dataList = new ArrayList<>();

        for (IncomeAttachment attachment : message.getAttachments()) {
            dataList.addAll(parseAttachment(attachment));
        }

        return dataList;
    }

    private Collection<Data> parseAttachment(IncomeAttachment attachment) {
        List<Data> data = new ArrayList<>(1);

        if (attachment instanceof IncomeImageAttachment) {
            if (attachment instanceof KnownIncomeImageAttachment)
                data.add((ImageLoader) () -> load((KnownIncomeImageAttachment) attachment));
            else
                data.add((ImageLoader) () -> AbstractMessenger.this.load((IncomeImageAttachment) attachment));
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

    private ImageData load(KnownIncomeImageAttachment attachment) throws IOException {
        File file = loadBinary(attachment.getUrl());

        return new MessengerImageData(file, attachment.getWidth(), attachment.getHeight());
    }

    private ImageData load(IncomeImageAttachment attachment) throws IOException {
        File file = loadBinary(attachment.getUrl());

        return new MessengerUnknownImageData(file);
    }

    private File loadBinary(String url) throws IOException {
        File file = File.createTempFile("loadVkImage", null);
        file.deleteOnExit();

        IOUtils.copy(urlDownloader.load(url), new FileOutputStream(file));

        return file;
    }

    public abstract void sendMessage(OutcomeMessage message);
}
