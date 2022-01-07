package ru.spliterash.imageBot.domain.pipeline;

import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.def.CaseExecutor;
import ru.spliterash.imageBot.domain.def.CaseIO;
import ru.spliterash.imageBot.domain.def.annotation.NameUtils;
import ru.spliterash.imageBot.domain.def.bean.Bean;
import ru.spliterash.imageBot.domain.def.cases.markers.NoReadCase;
import ru.spliterash.imageBot.domain.entities.Data;
import ru.spliterash.imageBot.domain.exceptions.BotIOException;
import ru.spliterash.imageBot.domain.pipeline.loader.dataDownloader.loaders.DataLoader;
import ru.spliterash.imageBot.domain.utils.ThreadUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PipelineService implements Bean {
    private final CaseExecutor executor;
    private final ThreadUtils threadUtils;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public PipelineOutput run(PipelineInput input) {
        return _run((List<PipelineStep>) (List<?>) input.getSteps(), input.getDataLoaders());
    }

    @SuppressWarnings("rawtypes")
    private PipelineOutput _run(List<PipelineStep> steps, List<Data> input) {
        // Закончился ли цикл из нечитаюших кейсов(кейсы перестановки, удаления)
        boolean readCasesEnd = true;

        CaseIO data = new CaseIO(input);

        List<PipelineOutput.OperationCost> costsList = new ArrayList<>(steps.size());
        for (PipelineStep step : steps) {
            // Пока вначале у нас кейсы перестановок, мы не читаем для них данные, следовательно, не тратим лишний трафик.
            // Может быть такое что придёт 10 картинок, но пользователь хочет только 1, и тогда нам не придётся грузить 10 картинок в память сервера
            // Оптимизация
            if (!readCasesEnd || !(step.getExecutedCase() instanceof NoReadCase)) {
                readCasesEnd = false;
                // Загрузим дату нормально
                data = new CaseIO(threadUtils.mapAsyncBlocked(data.getValues(), d -> {
                    if (d instanceof DataLoader) {
                        try {
                            return ((DataLoader) d).load();
                        } catch (IOException e) {
                            throw new BotIOException(e);
                        }
                    } else
                        return d;
                }));
            }
            long start = System.currentTimeMillis();
            //noinspection unchecked
            data = executor.execute(
                    step.getExecutedCase(),
                    data,
                    step.getParams()
            );

            long end = System.currentTimeMillis() - start;

            costsList.add(new PipelineOutput.OperationCost(NameUtils.name(step.getExecutedCase().getClass()), end));
        }

        return new PipelineOutput(data, costsList);
    }


}
