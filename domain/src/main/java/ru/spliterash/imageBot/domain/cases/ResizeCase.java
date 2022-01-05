package ru.spliterash.imageBot.domain.cases;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.spliterash.imageBot.domain.def.annotation.VariableName;
import ru.spliterash.imageBot.domain.def.cases.typed.SimpleImageCase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;

import javax.validation.constraints.Positive;

public abstract class ResizeCase extends SimpleImageCase<ResizeCase.Input> {
    @Getter
    @SuperBuilder
    @RequiredArgsConstructor
    public static class Input extends CaseParams {
        @Positive
        @VariableName("пропорция")
        private final double proportion;
    }
}
