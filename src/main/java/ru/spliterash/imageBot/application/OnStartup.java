package ru.spliterash.imageBot.application;

import nu.pattern.OpenCV;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class OnStartup implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        OpenCV.loadShared();
    }
}
