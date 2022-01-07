package ru.spliterash.imageBot.domain.def;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.entities.Data;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public final class CaseIO {
    private final List<? extends Data> values;

    public <T extends Data> Container<T> get(Class<T> clazz) {
        //noinspection unchecked,rawtypes JAVA HELL
        Map<Boolean, List<? extends Data>> collect = (Map<Boolean, List<? extends Data>>) (Map) values
                .stream()
                .collect(Collectors.groupingBy((d) -> clazz.isAssignableFrom(d.getClass())));
        //noinspection unchecked
        return new Container<>((List<T>)
                collect.getOrDefault(true, Collections.emptyList()),
                collect.getOrDefault(false, Collections.emptyList())
        );
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Container<T extends Data> {
        /**
         * Искомый тип данных
         */
        private final List<T> needData;
        /**
         * Оставшийся тип данных
         */
        private final List<? extends Data> restData;
    }
}
