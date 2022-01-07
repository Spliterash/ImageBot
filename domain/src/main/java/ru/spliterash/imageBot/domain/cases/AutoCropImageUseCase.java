package ru.spliterash.imageBot.domain.cases;

import ru.spliterash.imageBot.domain.def.annotation.Name;
import ru.spliterash.imageBot.domain.def.cases.typed.SimpleImageCase;
import ru.spliterash.imageBot.domain.def.params.EmptyParams;

@Name("Автоматическая обрезка объекта")
public abstract class AutoCropImageUseCase extends SimpleImageCase<EmptyParams> {
}
