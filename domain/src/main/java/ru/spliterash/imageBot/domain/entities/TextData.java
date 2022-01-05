package ru.spliterash.imageBot.domain.entities;

import lombok.RequiredArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class TextData implements Data {
    private final String text;

    @Override
    public InputStream read() {
        return new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    }
}
