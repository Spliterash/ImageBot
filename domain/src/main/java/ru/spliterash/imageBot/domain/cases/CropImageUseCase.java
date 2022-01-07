package ru.spliterash.imageBot.domain.cases;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.spliterash.imageBot.domain.def.annotation.Name;
import ru.spliterash.imageBot.domain.def.cases.typed.SimpleImageCase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;

@Name("Ручная обрезка")
public abstract class CropImageUseCase extends SimpleImageCase<CropImageUseCase.Params> {
    @Getter
    @SuperBuilder
    @RequiredArgsConstructor
    public static class Params extends CaseParams {
        private final Integer all;

        private final Integer left;
        private final Integer right;
        private final Integer top;
        private final Integer bottom;
    }
}
