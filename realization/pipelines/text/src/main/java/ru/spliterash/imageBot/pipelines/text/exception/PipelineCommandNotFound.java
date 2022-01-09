package ru.spliterash.imageBot.pipelines.text.exception;

import lombok.Getter;
import ru.spliterash.imageBot.domain.exceptions.ImageBotBaseException;

@Getter
public class PipelineCommandNotFound extends ImageBotBaseException {
    private final String cmd;

    public PipelineCommandNotFound(String notFoundCommand) {
        super("Процедурная команда " + notFoundCommand + " не найдена");
        this.cmd = notFoundCommand;
    }
}
