package ru.spliterash.imageBot.domain.cases;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.spliterash.imageBot.domain.def.annotation.Name;
import ru.spliterash.imageBot.domain.def.cases.typed.SimpleImageCase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.exceptions.CaseErrorException;

@Name("Обрезка изображения")
public abstract class CoverImageUseCase extends SimpleImageCase<CoverImageUseCase.Input> {
    @Getter
    @SuperBuilder
    @AllArgsConstructor
    public static class Input extends CaseParams {
        @Builder.Default
        @Name("ширина")
        private final int width = -1;
        @Builder.Default
        @Name("высота")
        private final int height = -1;

        /**
         * Отрезаем ли ту часть изображения, что не поместилась в новые размеры, или там будет просто прозрачно
         */
        @Builder.Default
        private final boolean cutImage = true;
    }

    public static class SpecifySizeException extends CaseErrorException {
        public SpecifySizeException() {
            super("Выбери размер картинки");
        }
    }
}
