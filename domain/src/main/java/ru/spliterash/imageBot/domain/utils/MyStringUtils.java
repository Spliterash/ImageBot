package ru.spliterash.imageBot.domain.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MyStringUtils {
    public String firstWord(String line) {
        int spaceIndex = line.indexOf(" ");

        return spaceIndex > -1 ? line.substring(0, spaceIndex) : line;
    }
}
