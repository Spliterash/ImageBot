package ru.spliterash.imageBot.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = "ru.spliterash.**"
)
public class ImageBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImageBotApplication.class, args);
    }
}
