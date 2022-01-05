package ru.spliterash.pictureBot.test;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;

@Getter
@RequiredArgsConstructor
public class TestFile {
    private final String name;
    private final InputStream stream;
}
