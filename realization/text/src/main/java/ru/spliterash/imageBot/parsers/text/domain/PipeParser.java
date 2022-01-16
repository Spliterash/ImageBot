package ru.spliterash.imageBot.parsers.text.domain;

import ru.spliterash.imageBot.domain.pipeline.PipelineStep;
import ru.spliterash.imageBot.parsers.text.domain.exceptions.PipeTextParseException;

import java.util.List;

public interface PipeParser {
    /**
     * На вход строка, на выход шаги пайпов
     */
    List<PipelineStep<?, ?>> parse(String str) throws PipeTextParseException;
}
