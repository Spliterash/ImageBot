package ru.spliterash.imageBot.domain.cases;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.spliterash.imageBot.domain.def.ImageCase;
import ru.spliterash.imageBot.domain.def.annotation.VariableName;
import ru.spliterash.imageBot.domain.def.params.CaseParams;

import javax.validation.constraints.Positive;

public interface CoverImageUseCase extends ImageCase<CoverImageUseCase.Input> {
    @Getter
    @SuperBuilder
    @AllArgsConstructor
    class Input extends CaseParams {
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
