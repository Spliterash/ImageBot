package ru.spliterash.imageBot.domain.def.bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Костыль, который говорит что этот бин не надо создавать автоматически, даже если класс им помечен
 * Его создавать только ручками из-за специфичных аргументов
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NoAutoCreate {

}
