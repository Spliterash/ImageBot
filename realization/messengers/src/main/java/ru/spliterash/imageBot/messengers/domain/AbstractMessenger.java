package ru.spliterash.imageBot.messengers.domain;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import ru.spliterash.imageBot.domain.def.CaseIO;
import ru.spliterash.imageBot.domain.def.bean.Bean;
import ru.spliterash.imageBot.domain.entities.Data;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.domain.entities.TextData;
import ru.spliterash.imageBot.domain.pipeline.PipelineOutput;
import ru.spliterash.imageBot.domain.pipeline.PipelineService;
import ru.spliterash.imageBot.domain.pipeline.PipelineStep;
import ru.spliterash.imageBot.domain.utils.ThreadUtils;
import ru.spliterash.imageBot.messengers.domain.attachment.income.IncomeAttachment;
import ru.spliterash.imageBot.messengers.domain.attachment.income.IncomeImageAttachment;
import ru.spliterash.imageBot.messengers.domain.attachment.income.IncomePostAttachment;
import ru.spliterash.imageBot.messengers.domain.attachment.income.KnownIncomeImageAttachment;
import ru.spliterash.imageBot.messengers.domain.dataDownloader.DataDownloader;
import ru.spliterash.imageBot.messengers.domain.dataDownloader.types.Empty;
import ru.spliterash.imageBot.messengers.domain.exceptions.MessengerException;
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

        List<DataDownloader<?>> downloaders = transfer(message);

        long downloadStart = System.currentTimeMillis();
        List<Data> downloaded = multiThreadDownload(downloaders);
        long downloadTime = System.currentTimeMillis() - downloadStart;

        long start = System.currentTimeMillis();
        List<PipelineStep<?, ?>> list = generator.parse(text);
        long parseEnd = System.currentTimeMillis() - start;
        start = System.currentTimeMillis();
        PipelineOutput result = pipelineService.run(list, new CaseIO(downloaded));
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
                        "Готово. Время парсинга: {0}\n" +
                                "Время всех пайплайнов: {1}\n" +
                                "Время загрузки изображений: {2}\n" +
                                "{3}",
                        String.format("%.3f", (parseEnd / 1000D)),
                        String.format("%.3f", (pipeEnd / 1000D)),
                        String.format("%.3f", (downloadTime / 1000D)),
                        builder.toString())
                )
                .build();

        sendMessage(outcome);
    }

    private List<Data> singleThreadDownload(List<DataDownloader<?>> list) {
        List<Data> result = new ArrayList<>(list.size());

        for (DataDownloader<?> downloader : list) {
            try {
                result.add(downloader.load());
            } catch (IOException e) {
                throw new MessengerException("Ошибка загрузки данных", e);
            }
        }

        return result;
    }

    private List<Data> multiThreadDownload(List<DataDownloader<?>> list) {
        return threadUtils.mapAsyncBlocked(list, d -> {
            try {
                return d.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private List<DataDownloader<?>> transfer(IncomeMessage message) {
        List<DataDownloader<?>> dataList = new ArrayList<>();

        for (IncomeAttachment attachment : message.getAttachments()) {
            dataList.addAll(parseAttachment(attachment));
        }

        return dataList;
    }

    private Collection<DataDownloader<?>> parseAttachment(IncomeAttachment attachment) {
        List<DataDownloader<?>> data = new ArrayList<>(1);

        if (attachment instanceof IncomeImageAttachment) {
            if (attachment instanceof KnownIncomeImageAttachment)
                data.add(() -> load((KnownIncomeImageAttachment) attachment));
            else
                data.add(() -> load((IncomeImageAttachment) attachment));
        } else if (attachment instanceof IncomePostAttachment)
            data.addAll(parsePost((IncomePostAttachment) attachment));

        return data;
    }

    private Collection<DataDownloader<?>> parsePost(IncomePostAttachment post) {
        List<DataDownloader<?>> data = new ArrayList<>(post.getAttachments().size());
        if (StringUtils.isNotEmpty(post.getText()))
            data.add(new Empty<>(new TextData(post.getText())));
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
