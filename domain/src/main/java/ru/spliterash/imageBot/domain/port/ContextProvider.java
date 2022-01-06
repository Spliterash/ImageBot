package ru.spliterash.imageBot.domain.port;

public interface ContextProvider {
    <T> T get(Class<T> beanClass);
}
