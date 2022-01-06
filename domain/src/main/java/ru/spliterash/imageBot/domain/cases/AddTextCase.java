package ru.spliterash.imageBot.domain.cases;

import ru.spliterash.imageBot.domain.def.cases.AddDataCase;
import ru.spliterash.imageBot.domain.entities.Data;
import ru.spliterash.imageBot.domain.entities.TextData;

public class AddTextCase extends AddDataCase<String> {
    @Override
    protected Data wrap(String data) {
        return new TextData(data);
    }
}
