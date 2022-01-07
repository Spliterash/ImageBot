package ru.spliterash.imageBot.messengers.domain.wrappers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.domain.exceptions.CaseErrorException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@RequiredArgsConstructor
public class MessengerImageData implements ImageData {
    private final File image;
    @Getter
    private final int width;
    @Getter
    private final int height;

    @Override
    public InputStream read() {
        try {
            return new FileInputStream(image);
        } catch (FileNotFoundException e) {
            throw new CaseErrorException("Ошибка загрузки файла");
        }
    }
}
