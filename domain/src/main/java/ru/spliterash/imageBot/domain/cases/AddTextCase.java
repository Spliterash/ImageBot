package ru.spliterash.imageBot.domain.cases;

import ru.spliterash.imageBot.domain.def.annotation.Name;
import ru.spliterash.imageBot.domain.def.cases.AddDataCase;
import ru.spliterash.imageBot.domain.def.cases.markers.NoReadCase;
import ru.spliterash.imageBot.domain.entities.Data;
import ru.spliterash.imageBot.domain.entities.TextData;

@Name(
        value = "Добавление текста",
        info = "Добавляет текст в текущий массив данных"
)
public class AddTextCase extends AddDataCase<String> implements NoReadCase {
    @Override
    protected Data wrap(String data) {
        return new TextData(data);
    }
}
