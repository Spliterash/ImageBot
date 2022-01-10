package ru.spliterash.imageBot.pipelines.text.exception;

import ru.spliterash.imageBot.domain.def.ImagePipelineCase;
import ru.spliterash.imageBot.domain.def.annotation.NameUtils;
import ru.spliterash.imageBot.domain.exceptions.ImageBotBaseException;

public class CaseParserNotFound extends ImageBotBaseException {
    public CaseParserNotFound(Class<? extends ImagePipelineCase<?>> clazz) {
        super("Не удалось найти обработчик команд для " + NameUtils.name(clazz));
    }
}
