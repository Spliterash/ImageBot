package ru.spliterash.imageBot.domain.cases;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.spliterash.imageBot.domain.def.annotation.Name;
import ru.spliterash.imageBot.domain.def.cases.markers.VeryLongCase;
import ru.spliterash.imageBot.domain.def.cases.typed.SimpleImageCase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;

import javax.validation.constraints.NotNull;
import java.util.List;

@Name(value = "ESRGAN", info = "Пропускает картинку через ESRGAN")
public abstract class ESRGANCase extends SimpleImageCase<ESRGANCase.Params> implements VeryLongCase {

    public abstract List<String> models();

    @Getter
    @SuperBuilder
    @RequiredArgsConstructor
    public static class Params extends CaseParams {
        @NotNull
        private final Integer model;
    }
}
