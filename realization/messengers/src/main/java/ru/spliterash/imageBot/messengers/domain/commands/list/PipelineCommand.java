package ru.spliterash.imageBot.messengers.domain.commands.list;

import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.def.ImagePipelineCase;
import ru.spliterash.imageBot.domain.def.annotation.NameUtils;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.domain.entities.defaultEnt.FileImage;
import ru.spliterash.imageBot.domain.exceptions.WrongPipelineInputException;
import ru.spliterash.imageBot.domain.pipeline.PipelineInput;
import ru.spliterash.imageBot.domain.pipeline.PipelineOutput;
import ru.spliterash.imageBot.domain.pipeline.PipelineService;
import ru.spliterash.imageBot.domain.pipeline.PipelineStep;
import ru.spliterash.imageBot.domain.pipeline.loaders.ImageLoader;
import ru.spliterash.imageBot.domain.utils.MyStringUtils;
import ru.spliterash.imageBot.messengers.domain.AbstractMessenger;
import ru.spliterash.imageBot.messengers.domain.attachment.income.IncomeAttachment;
import ru.spliterash.imageBot.messengers.domain.attachment.income.IncomeImageAttachment;
import ru.spliterash.imageBot.messengers.domain.attachment.income.IncomePostAttachment;
import ru.spliterash.imageBot.messengers.domain.attachment.income.KnownIncomeImageAttachment;
import ru.spliterash.imageBot.messengers.domain.commands.BotCommand;
import ru.spliterash.imageBot.messengers.domain.message.income.IncomeMessage;
import ru.spliterash.imageBot.messengers.domain.message.outcome.OutcomeMessage;
import ru.spliterash.imageBot.pipelines.text.CLIPipelineGenerator;
import ru.spliterash.imageBot.pipelines.text.def.CLICaseParser;
import ru.spliterash.imageBot.pipelines.text.exception.PipelineCommandNotFound;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PipelineCommand implements BotCommand {
    private final List<CLICaseParser<?, ?>> parsers;
    private final CLIPipelineGenerator generator;
    private final PipelineService pipelineService;

    @Override
    public List<String> getAliases() {
        return Arrays.asList(
                "????????",
                "????????????????",
                "pipe",
                "pipeline",
                "????????????????"
        );
    }


    @Override
    public String help() {
        StringBuilder builder = new StringBuilder();
        builder
                .append("?????????????? ?????????????????????? ???????????????? ?????? ??????????????????????. ??????????????????????????:" + "\n" + "<awakening> ")
                .append(aliasLine()).append("\n").append("???????????????? 1 -?? ???????????????? 1").append("\n")
                .append("???????????????? 2 -?? ???????????????? 2").append("\n")
                .append("???????????????? 3 -?? ???????????????? 3").append("\n")
                .append("-------").append("\n");
        for (CLICaseParser<?, ?> parser : parsers) {
            builder.append(parser.help()).append("\n============\n");
        }

        return builder.toString();
    }

    @Override
    public void execute(AbstractMessenger messenger, IncomeMessage message, String[] args, String[] anotherLines) {
        long start = System.currentTimeMillis();

        List<ImageData> loaders = transfer(messenger, message);

        String[] lines;
        if (anotherLines.length == 0)
            lines = String.join(" ", args).split(" *, *"); // ?????????????????? ??????????????, ?????? ?????? ?????????????? ???? ?????????? ?????????? ?????????? enter :(
        else
            lines = anotherLines;
        List<PipelineStep<?, ?>> list = generator.parse(lines);
        PipelineOutput result = pipelineService.run(new PipelineInput(list, loaders));

        StringBuilder builder = new StringBuilder();
        for (PipelineOutput.OperationCost cost : result.getCost()) {
            builder.append(cost.getOperationName()).append(": ").append(String.format("%.3f", (cost.getCost() / 1000D))).append("\n");
        }

        long end = System.currentTimeMillis() - start;
        OutcomeMessage outcome = OutcomeMessage.builder()
                .attachments(result.getOutput())
                .peerId(message.getPeerId())
                .replyTo(message.getId())
                .text(MessageFormat.format(
                        "?????????? ???????????????????? ???? ?????????????????? ???? ????????????????: {0}\n" +
                                "{1}",
                        String.format("%.3f", (end / 1000D)),
                        builder.toString())
                )
                .build();

        messenger.sendMessage(outcome);
    }


    public ImageData load(AbstractMessenger messenger, IncomeImageAttachment attachment) throws IOException {
        File file = messenger.loadBinary(attachment.getUrl());

        return new FileImage(file);
    }

    private Collection<ImageData> parseAttachment(AbstractMessenger messenger, IncomeAttachment attachment) {
        List<ImageData> data = new ArrayList<>(1);

        if (attachment instanceof IncomeImageAttachment) {
            if (attachment instanceof KnownIncomeImageAttachment)
                data.add((ImageLoader) () -> load(messenger, (KnownIncomeImageAttachment) attachment));
            else
                data.add((ImageLoader) () -> load(messenger, (IncomeImageAttachment) attachment));
        } else if (attachment instanceof IncomePostAttachment)
            data.addAll(parsePost(messenger, (IncomePostAttachment) attachment));

        return data;
    }

    private Collection<ImageData> parsePost(AbstractMessenger messenger, IncomePostAttachment post) {
        List<ImageData> data = new ArrayList<>(post.getAttachments().size());
        for (IncomeAttachment attachment : post.getAttachments()) {
            data.addAll(parseAttachment(messenger, attachment));
        }
        return data;
    }

    @Override
    public void handleException(Exception exception, AbstractMessenger messenger, String peer) {
        if (exception instanceof WrongPipelineInputException) {
            CLICaseParser<? extends ImagePipelineCase<?>, ?> commandParse = generator.findCase(((WrongPipelineInputException) exception).getCaseClass());
            String msg = exception.getLocalizedMessage();

            messenger.sendMessage(OutcomeMessage.builder()
                    .peerId(peer)
                    .text(String.format(
                            "???????????????? ???????????????? ?????? ???????????????????? ???????????????? \"%s\".\n???????????????? ????????????: %s\n%s",
                            NameUtils.name(commandParse.getCase().getClass()),
                            msg,
                            commandParse.help()
                    ))
                    .build());
        } else if (exception instanceof PipelineCommandNotFound) {
            messenger.sendMessage(peer, String.format(
                    "???? ?????????????? ?????????? ?????????????????????? ?????????????? %s. " +
                            "???????????????????? ???????????????????????????? <awaking> ???????????? ?????? ?????????????????? ???????????? ???????????????? ?? ????????????",
                    ((PipelineCommandNotFound) exception).getCmd())
            );
        } else
            messenger.sendMessage(peer, MyStringUtils.exceptionWrite(exception));
    }

    public List<ImageData> transfer(AbstractMessenger messenger, IncomeMessage message) {
        List<ImageData> dataList = message
                .getReply()
                .stream()
                .map(innerMessage -> transfer(messenger, innerMessage))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        for (IncomeAttachment attachment : message.getAttachments()) {
            dataList.addAll(parseAttachment(messenger, attachment));
        }

        return dataList;
    }

    private ImageData load(AbstractMessenger messenger, KnownIncomeImageAttachment attachment) throws IOException {
        File file = messenger.loadBinary(attachment.getUrl());

        return new FileImage(file, attachment.getWidth(), attachment.getHeight());
    }
}
