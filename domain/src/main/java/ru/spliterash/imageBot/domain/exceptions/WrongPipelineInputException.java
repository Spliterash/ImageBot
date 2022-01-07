package ru.spliterash.imageBot.domain.exceptions;

import lombok.Getter;
import ru.spliterash.imageBot.domain.def.PipelineCase;

/**
 * Ошибка возникающая при неправильных аргументах
 */
@Getter
public class WrongPipelineInputException extends CaseErrorException {
    private final Class<? extends PipelineCase<?>> caseClass;

    public WrongPipelineInputException(String message, Class<? extends PipelineCase<?>> caseClass) {
        super(message);
        this.caseClass = caseClass;
    }
}
