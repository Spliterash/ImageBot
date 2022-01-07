package ru.spliterash.imageBot.messengers.domain.wrappers;

import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.domain.exceptions.CaseErrorException;
import ru.spliterash.imageBot.domain.utils.ImageUtils;

import java.awt.*;
import java.io.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RequiredArgsConstructor
public class MessengerUnknownImageData implements ImageData {
    private final File file;
    private transient int width = -1;
    private transient int height = -1;

    private final transient Lock lock = new ReentrantLock();

    @Override
    public InputStream read() {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new CaseErrorException("Ошибка загрузки файла");
        }
    }


    private void sizesFill() {
        lock.lock();
        try {
            if (width != -1)
                return;
            try {
                Dimension img = ImageUtils.getImageDimension(new FileInputStream(file));

                width = img.width;
                height = img.height;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getWidth() {
        sizesFill();
        return width;
    }

    @Override
    public int getHeight() {
        sizesFill();
        return height;
    }
}
