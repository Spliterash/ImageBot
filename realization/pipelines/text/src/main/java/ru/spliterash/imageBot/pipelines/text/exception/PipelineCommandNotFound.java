package ru.spliterash.imageBot.pipelines.text.exception;

import ru.spliterash.imageBot.domain.exceptions.ImageBotBaseException;

public class PipelineCommandNotFound extends ImageBotBaseException {
    public PipelineCommandNotFound(String notFoundCommand) {
        super("Процедурная команда " + notFoundCommand + " не найдена");
    }
}
