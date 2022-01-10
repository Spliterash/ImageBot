package ru.spliterash.imageBot.domain.exceptions;

import lombok.Getter;
import ru.spliterash.imageBot.domain.def.ImagePipelineCase;

/**
 * Ошибка возникающая при неправильных аргументах
 */
@Getter
public class WrongPipelineInputException extends CaseErrorException {
    private final Class<? extends ImagePipelineCase<?>> caseClass;

    public WrongPipelineInputException(String message, Class<? extends ImagePipelineCase<?>> caseClass) {
        super(message);
        this.caseClass = caseClass;
    }
}
