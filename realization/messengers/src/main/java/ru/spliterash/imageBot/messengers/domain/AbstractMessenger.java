package ru.spliterash.imageBot.messengers.domain;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import ru.spliterash.imageBot.domain.def.bean.Bean;
import ru.spliterash.imageBot.domain.pipeline.PipelineService;
import ru.spliterash.imageBot.domain.utils.MyStringUtils;
import ru.spliterash.imageBot.domain.utils.ThreadUtils;
import ru.spliterash.imageBot.messengers.domain.commands.BotCommand;
import ru.spliterash.imageBot.messengers.domain.exceptions.SpecifyCommandException;
import ru.spliterash.imageBot.messengers.domain.message.income.IncomeMessage;
import ru.spliterash.imageBot.messengers.domain.message.outcome.OutcomeMessage;
import ru.spliterash.imageBot.messengers.domain.port.URLDownloader;
import ru.spliterash.imageBot.pipelines.text.TextPipelineGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
public abstract class AbstractMessenger implements Bean {
    protected final TextPipelineGenerator generator;
    protected final URLDownloader urlDownloader;
    protected final PipelineService pipelineService;
    protected final ThreadUtils threadUtils;
    protected final Set<BotCommand> commands;

    protected final ExecutorService pool = Executors.newFixedThreadPool(6);
    private static final Set<String> AWAKING_ALIAS = Set.of("пикча", "картинка", "приём");
    private static final Set<String> HELP = Set.of("помощь", "help", "памагити");

    /**
     * Присылать сюда только те сообщения, которые нужно обработать и содержащие только текст обработки
     */
    protected void notifyMessage(IncomeMessage message) {
        String peerId = message.getPeerId();
        String origText = message.getText();
        String[] split = origText.split("[\r\n]+");

        String str = split[0].trim().toLowerCase();

        String[] firstLineSplit = str.split(" +");

        if (firstLineSplit.length == 0)
            return;

        String firstWord = firstLineSplit[0];


        if (AWAKING_ALIAS.stream().noneMatch(a -> a.equals(firstWord)))
            if (!needProcess(firstWord))
                return;

        if (firstLineSplit.length < 2)
            throw new SpecifyCommandException();

        String command = firstLineSplit[1];

        BotCommand executor = commands
                .stream()
                .filter(c -> c.getAliases().stream().anyMatch(a -> a.equals(command)))
                .findFirst()
                .orElse(null);

        if (executor == null) {
            commandNotFound(peerId, command);
            return;
        }

        String[] args = Arrays.copyOfRange(firstLineSplit, 2, firstLineSplit.length);
        String[] anotherLines = Arrays.copyOfRange(split, 1, split.length);

        if (args.length > 0 && HELP.contains(args[0]))
            sendMessage(peerId, executor.help());
        else {
            try {
                executor.execute(this, message, args, anotherLines);
            } catch (Exception exception) {
                executor.handleException(exception, this, peerId);
            }
        }
    }

    protected abstract boolean needProcess(String firstWord);


    public File loadBinary(String url) throws IOException {
        File file = File.createTempFile("loadMessengerImage", null);
        file.deleteOnExit();

        IOUtils.copy(urlDownloader.load(url), new FileOutputStream(file));

        return file;
    }

    public abstract void sendMessage(OutcomeMessage message);

    public void sendMessage(String peer, String message) {
        sendMessage(OutcomeMessage.builder()
                .peerId(peer)
                .text(message)
                .build());
    }

    protected void commandNotFound(String peerId, String errorCommand) {
        sendMessage(peerId, "Команда " + errorCommand + " не найдена");
    }

    protected void notifyInternalError(Throwable exception, String peerId) {
        sendMessage(peerId, MyStringUtils.exceptionWrite(exception));
    }
}
