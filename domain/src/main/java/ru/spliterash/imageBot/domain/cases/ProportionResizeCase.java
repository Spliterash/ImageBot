package ru.spliterash.imageBot.domain.cases;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.spliterash.imageBot.domain.def.annotation.Name;
import ru.spliterash.imageBot.domain.def.cases.SimpleImageCase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;

import javax.validation.constraints.Positive;

@Name(
        value = "Пропорциональное изменение размера",
        info = "Пропорционально изменяет размер изображения"
)
public abstract class ProportionResizeCase extends SimpleImageCase<ProportionResizeCase.Input> {
    @Getter
    @SuperBuilder
    @RequiredArgsConstructor
    public static class Input extends CaseParams {
        @Positive
        @Name("пропорция")
        private final double proportion;
    }
}
