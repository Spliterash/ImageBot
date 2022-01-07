package ru.spliterash.imageBot.pipelines.text.utils;

import ru.spliterash.imageBot.pipelines.text.exception.CommandParseException;

import java.util.Arrays;
import java.util.function.Consumer;

public class ParseUtils {
    public boolean parseBoolean(String val) {
        switch (val.toLowerCase()) {
            case "1":
            case "true":
            case "y":
            case "yes":
            case "да":
                return true;
            case "0":
            case "false":
            case "n":
            case "no":
            case "нет":
                return false;
            default:
                throw new CommandParseException("Некорректный тип boolean: " + val);
        }
    }

    public int parseInt(String val) {
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException ex) {
            throw new CommandParseException("Некорректный целочисленный тип: " + val);
        }
    }

    public <T> void setIfPresent(T t, Consumer<T> setter) {
        if (t != null)
            setter.accept(t);
    }

    public <T extends Enum<?>> T parseEnum(Class<T> enumClass, String s) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(c -> c.name().equalsIgnoreCase(s))
                .findFirst()
                .orElseThrow(() -> new CommandParseException("Некорректный enum тип: " + s));
    }
}
