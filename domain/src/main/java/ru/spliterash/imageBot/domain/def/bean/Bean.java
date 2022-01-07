package ru.spliterash.imageBot.domain.def.bean;

/**
 * Маркерный интерфейс. Говорит о том, что надо этот класс положить в контекст
 */
public interface Bean {
    default void postConstruct() {
    }

    default void preDestroy() {
    }
}
