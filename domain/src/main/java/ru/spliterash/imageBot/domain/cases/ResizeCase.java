package ru.spliterash.imageBot.domain.cases;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.spliterash.imageBot.domain.def.ImageCase;
import ru.spliterash.imageBot.domain.def.annotation.VariableName;
import ru.spliterash.imageBot.domain.def.params.CaseParams;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Positive;

public interface ResizeCase extends ImageCase<ResizeCase.Input> {
    @Getter
    @SuperBuilder
    @RequiredArgsConstructor
    class Input extends CaseParams {
        @Positive
        @VariableName("пропорция")
        @DecimalMax("1.0")
        private final double proportion;
    }
}
