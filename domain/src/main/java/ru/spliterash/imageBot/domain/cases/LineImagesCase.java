package ru.spliterash.imageBot.domain.cases;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.spliterash.imageBot.domain.def.annotation.Name;
import ru.spliterash.imageBot.domain.def.cases.MultiImageCase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.entities.Direction;

import javax.validation.constraints.NotNull;

@Name(
        value = "Склеивание в одну линию",
        info = "Почти как обычный клей, но склеивание идёт в одну линию без потери деталей"
)
public abstract class LineImagesCase extends MultiImageCase<LineImagesCase.Params> {

    @SuperBuilder
    @Getter
    public static class Params extends CaseParams {
        @Name("границы")
        @Builder.Default
        private final boolean needBorder = true;
        @Builder.Default
        @NotNull
        private final Direction direction = Direction.VERTICAL;
    }
}
