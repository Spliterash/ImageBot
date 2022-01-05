package ru.spliterash.imageBot.domain.exceptions;

import lombok.Getter;
import ru.spliterash.imageBot.domain.def.params.CaseParams;

import javax.validation.ConstraintViolation;
import java.util.Set;

@Getter
public class CaseValidateException extends CaseErrorException {
    private final Set<ConstraintViolation<? extends CaseParams>> errors;

    public CaseValidateException(String message, Set<ConstraintViolation<? extends CaseParams>> errors) {
        super(message);
        this.errors = errors;
    }
}
