package ru.spliterash.imageBot.domain.cases;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.spliterash.imageBot.domain.def.annotation.VariableName;
import ru.spliterash.imageBot.domain.def.cases.typed.SimpleImageCase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;

import javax.validation.constraints.Positive;

public abstract class CoverImageUseCase extends SimpleImageCase<CoverImageUseCase.Input> {
    @Getter
    @SuperBuilder
    @AllArgsConstructor
    public static class Input extends CaseParams {
        @Positive
        @VariableName("ширина")
        private final int width;
        @Positive
        @VariableName("высота")
        private final int height;

        /**
         * Отрезаем ли ту часть изображения, что не поместилась в новые размеры, или там будет просто прозрачно
         */
        @Builder.Default
        private final boolean cutImage = false;
    }
}
