package ru.spliterash.imageBot.domain.utils;

import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.def.bean.Bean;
import ru.spliterash.imageBot.domain.exceptions.ImageBotBaseException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ThreadUtils implements Bean {
    private final Executor executor;

    /**
     * Блокирует текущий поток, выполняя задачу в других потоках
     * Полезно когда надо например загрузить файлы, или выгрузить
     */
    public <I, O> List<O> mapAsyncBlocked(Collection<I> input, Function<I, O> map) {
        if (input.size() == 1)
            return input
                    .stream()
                    .map(map)
                    .collect(Collectors.toList());

        List<Pos<O>> result = new ArrayList<>(input.size());
        CountDownLatch countDownLatch = new CountDownLatch(input.size());

        int count = 0;
        for (I i : input) {
            int current = count++;
            executor.execute(() -> {
                O o = map.apply(i);
                result.add(new Pos<>(o, current));
                countDownLatch.countDown();
            });
        }

        try {
            countDownLatch.await();

            return result
                    .stream()
                    .sorted(Comparator.comparingInt(s -> s.i))
                    .map(s -> s.o)
                    .collect(Collectors.toList());
        } catch (InterruptedException e) {
            throw new ImageBotBaseException("Потоки поругались", e);
        }
    }

    @RequiredArgsConstructor
    private static class Pos<O> {
        private final O o;
        private final int i;
    }
}
