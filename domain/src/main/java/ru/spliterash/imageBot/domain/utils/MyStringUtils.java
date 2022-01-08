package ru.spliterash.imageBot.domain.utils;

import lombok.experimental.UtilityClass;

import java.io.PrintWriter;
import java.io.StringWriter;

@UtilityClass
public class MyStringUtils {
    public String firstWord(String line) {
        int spaceIndex = line.indexOf(" ");

        return spaceIndex > -1 ? line.substring(0, spaceIndex) : line;
    }

    public String exceptionWrite(Throwable exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        exception.printStackTrace(writer);

        return stringWriter.toString();
    }
}
