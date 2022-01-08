package ru.spliterash.imageBot.domain.def.bean;

/**
 * Маркерный интерфейс. Говорит о том, что надо этот класс положить в контекст
 */
@SuppressWarnings("RedundantThrows")
public interface Bean {
    default void postConstruct() throws Exception {
    }

    default void preDestroy() throws Exception {
    }
}
