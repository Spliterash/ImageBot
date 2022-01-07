package ru.spliterash.imageBot.pipelines.text;

import ru.spliterash.imageBot.domain.def.bean.Bean;
import ru.spliterash.imageBot.domain.entities.Data;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.domain.entities.TextData;

import java.util.Map;

public class TextTypes implements Bean {
    public static final String IMAGE = "image";
    public static final String TEXT = "text";

    public static final Map<String, Class<? extends Data>> TYPE_MAP = Map.of(
            IMAGE, ImageData.class,
            TEXT, TextData.class
    );
}
