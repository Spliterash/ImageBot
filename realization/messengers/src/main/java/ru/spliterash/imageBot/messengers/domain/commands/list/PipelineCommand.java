package ru.spliterash.imageBot.messengers.domain.commands.list;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import ru.spliterash.imageBot.domain.def.PipelineCase;
import ru.spliterash.imageBot.domain.def.annotation.NameUtils;
import ru.spliterash.imageBot.domain.entities.Data;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.domain.entities.TextData;
import ru.spliterash.imageBot.domain.exceptions.WrongPipelineInputException;
import ru.spliterash.imageBot.domain.pipeline.PipelineInput;
import ru.spliterash.imageBot.domain.pipeline.PipelineOutput;
import ru.spliterash.imageBot.domain.pipeline.PipelineService;
import ru.spliterash.imageBot.domain.pipeline.PipelineStep;
import ru.spliterash.imageBot.domain.pipeline.loaders.ImageLoader;
import ru.spliterash.imageBot.messengers.domain.AbstractMessenger;
import ru.spliterash.imageBot.messengers.domain.attachment.income.IncomeAttachment;
import ru.spliterash.imageBot.messengers.domain.attachment.income.IncomeImageAttachment;
import ru.spliterash.imageBot.messengers.domain.attachment.income.IncomePostAttachment;
import ru.spliterash.imageBot.messengers.domain.attachment.income.KnownIncomeImageAttachment;
import ru.spliterash.imageBot.messengers.domain.commands.BotCommand;
import ru.spliterash.imageBot.messengers.domain.message.income.IncomeMessage;
import ru.spliterash.imageBot.messengers.domain.message.outcome.OutcomeMessage;
import ru.spliterash.imageBot.messengers.domain.wrappers.MessengerImageData;
import ru.spliterash.imageBot.messengers.domain.wrappers.MessengerUnknownImageData;
import ru.spliterash.imageBot.pipelines.text.TextPipelineGenerator;
import ru.spliterash.imageBot.pipelines.text.def.CaseTextParser;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class PipelineCommand implements BotCommand {
    private final List<CaseTextParser<?, ?>> parsers;
    private final TextPipelineGenerator generator;
    private final PipelineService pipelineService;

    @Override
    public List<String> getAliases() {
        return Arrays.asList(
                "пайп",
                "пайплайн",
                "pipe",
                "pipeline",
                "конвейер"
        );
    }


    @Override
    public String help() {
        StringBuilder builder = new StringBuilder();
        builder
                .append("Команда запускающая конвейер для изображений. Использование:" + "\n" + "<awakening> ")
                .append(aliasLine()).append("\n").append("операция 1 -а аргумент 1").append("\n")
                .append("операция 2 -а аргумент 2").append("\n")
                .append("операция 3 -а аргумент 3").append("\n")
                .append("-------").append("\n");
        for (CaseTextParser<?, ?> parser : parsers) {
            builder.append(parser.help()).append("\n============\n");
        }

        return builder.toString();
    }

    @Override
    public void execute(AbstractMessenger messenger, IncomeMessage message, String[] args, String[] anotherLines) {
        List<Data> loaders = transfer(messenger, message);

        long start = System.currentTimeMillis();
        List<PipelineStep<?, ?>> list = generator.parse(anotherLines);
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

        messenger.sendMessage(outcome);
    }


    public ImageData load(AbstractMessenger messenger, IncomeImageAttachment attachment) throws IOException {
        File file = messenger.loadBinary(attachment.getUrl());

        return new MessengerUnknownImageData(file);
    }

    private Collection<Data> parseAttachment(AbstractMessenger messenger, IncomeAttachment attachment) {
        List<Data> data = new ArrayList<>(1);

        if (attachment instanceof IncomeImageAttachment) {
            if (attachment instanceof KnownIncomeImageAttachment)
                data.add((ImageLoader) () -> load(messenger, (KnownIncomeImageAttachment) attachment));
            else
                data.add((ImageLoader) () -> load(messenger, (IncomeImageAttachment) attachment));
        } else if (attachment instanceof IncomePostAttachment)
            data.addAll(parsePost(messenger, (IncomePostAttachment) attachment));

        return data;
    }

    private Collection<Data> parsePost(AbstractMessenger messenger, IncomePostAttachment post) {
        List<Data> data = new ArrayList<>(post.getAttachments().size());
        if (StringUtils.isNotEmpty(post.getText()))
            data.add(new TextData(post.getText()));
        for (IncomeAttachment attachment : post.getAttachments()) {
            data.addAll(parseAttachment(messenger, attachment));
        }

        return data;
    }

    @Override
    public void handleException(Exception exception, AbstractMessenger messenger, String peer) {
        if (exception instanceof WrongPipelineInputException) {
            CaseTextParser<? extends PipelineCase<?>, ?> commandParse = generator.findCase(((WrongPipelineInputException) exception).getCaseClass());
            String msg = exception.getLocalizedMessage();

            messenger.sendMessage(OutcomeMessage.builder()
                    .peerId(peer)
                    .text(String.format(
                            "Возникла проблема при выполнении операции \"%s\".\nОписание ошибки: %s\n%s",
                            NameUtils.name(commandParse.getCase().getClass()),
                            msg,
                            commandParse.help()
                    ))
                    .build());
        } else
            messenger.sendMessage(OutcomeMessage.builder()
                    .peerId(peer)
                    .text(exception.getMessage())
                    .build());
    }

    public List<Data> transfer(AbstractMessenger messenger, IncomeMessage message) {
        List<Data> dataList = new ArrayList<>();

        for (IncomeAttachment attachment : message.getAttachments()) {
            dataList.addAll(parseAttachment(messenger, attachment));
        }

        return dataList;
    }


    private ImageData load(AbstractMessenger messenger, KnownIncomeImageAttachment attachment) throws IOException {
        File file = messenger.loadBinary(attachment.getUrl());

        return new MessengerImageData(file, attachment.getWidth(), attachment.getHeight());
    }
}