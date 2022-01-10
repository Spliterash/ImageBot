package ru.spliterash.imageBot.domain.cases;

import ru.spliterash.imageBot.domain.def.annotation.Name;
import ru.spliterash.imageBot.domain.def.cases.SimpleImageCase;
import ru.spliterash.imageBot.domain.def.params.EmptyParams;

@Name(
        value = "Автоматическая обрезка объекта",
        info = "Автоматически обрезает изображение используя AI"
)
public abstract class AutoCropImageUseCase extends SimpleImageCase<EmptyParams> {
}
