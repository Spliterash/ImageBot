package ru.spliterash.imageBot.domain.cases;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.spliterash.imageBot.domain.def.annotation.Name;
import ru.spliterash.imageBot.domain.def.cases.typed.MultiImageCase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Name("Склеивание изображения")
public abstract class GlueImagesCase extends MultiImageCase<GlueImagesCase.GlueImagesParams> {

    public enum ResizeMode {
        // Увеличивать маленькие изображения до размера большого
        // Размер клетки это средний размер
        COVER,
        // Вставлять как есть, рамки по размеру самого большого изображения
        // Размер клетки размер самого большого изображения
        NONE

    }

    @SuperBuilder
    @Getter
    public static class GlueImagesParams extends CaseParams {
        @Name("границы")
        @Builder.Default
        private final boolean needBorder = true;
        @Name("отступы")
        @PositiveOrZero
        @Builder.Default
        private final int padding = 0;
        @Name("кол-во столбцов")
        @Positive
        private final Integer columns;
        @Name("кол-во строк")
        @Positive
        private final Integer rows;
        @Name("режим склейки")
        @NotNull
        @Builder.Default
        private final ResizeMode resizeMode = ResizeMode.COVER;
        @Builder.Default
        @Name("максимальный размер картинки по обоим сторонам")
        private final int max = 2048;
    }
}
