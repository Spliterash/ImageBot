package ru.spliterash.imageBot.domain.def;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.entities.Data;
import ru.spliterash.imageBot.domain.entities.ImageData;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public final class CaseIO {
    private final List<? extends Data> values;

    public <T extends Data> Container<T> get(Class<T> clazz) {
        Map<Boolean, ? extends List<? extends Data>> collect = values
                .stream()
                .collect(Collectors.groupingBy((d) -> clazz.isAssignableFrom(d.getClass())));
        //noinspection unchecked
        return new Container<>((List<T>) collect.get(true), collect.get(false));
    }

    public Container<ImageData> images() {
        return get(ImageData.class);
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
