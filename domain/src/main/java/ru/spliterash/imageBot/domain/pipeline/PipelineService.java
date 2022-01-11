package ru.spliterash.imageBot.domain.pipeline;

import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.def.CaseExecutor;
import ru.spliterash.imageBot.domain.def.ImageCaseContext;
import ru.spliterash.imageBot.domain.def.ImagePipelineCase;
import ru.spliterash.imageBot.domain.def.annotation.NameUtils;
import ru.spliterash.imageBot.domain.def.bean.Bean;
import ru.spliterash.imageBot.domain.def.cases.markers.NoReadCase;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.domain.exceptions.BotExceptionWrapper;
import ru.spliterash.imageBot.domain.pipeline.loaders.ImageLoader;
import ru.spliterash.imageBot.domain.port.ContextProvider;
import ru.spliterash.imageBot.domain.utils.ThreadUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PipelineService implements Bean {
    private final CaseExecutor executor;
    private final ThreadUtils threadUtils;
    private final ContextProvider contextProvider;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public PipelineOutput run(PipelineInput input) {
        return _run((List<PipelineStep>) (List<?>) input.getSteps(), input.getDataLoaders());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private PipelineOutput _run(List<PipelineStep> steps, List<ImageData> input) {
        // Закончился ли цикл из нечитаюших кейсов(кейсы перестановки, удаления)
        boolean readCasesEnd = true;

        final ImageCaseContext context = new ImageCaseContext(input);

        List<PipelineOutput.OperationCost> costsList = new ArrayList<>(steps.size());
        for (PipelineStep step : steps) {
            // Пока вначале у нас кейсы перестановок, мы не читаем для них данные, следовательно, не тратим лишний трафик.
            // Может быть такое что придёт 10 картинок, но пользователь хочет только 1, и тогда нам не придётся грузить 10 картинок в память сервера
            // Оптимизация
            if (readCasesEnd) {
                if (!(NoReadCase.class.isAssignableFrom(step.getCaseClazz()))) {
                    readCasesEnd = false;
                    // Загрузим дату нормально
                    List<ImageData> images = threadUtils.mapAsyncBlocked(context.getImages(), d -> {
                        if (d instanceof ImageLoader) {
                            try {
                                return ((ImageLoader) d).load();
                            } catch (IOException e) {
                                throw new BotExceptionWrapper(e);
                            }
                        } else
                            return d;
                    });
                    context.set(images);
                }
            }

            long start = System.currentTimeMillis();
            ImagePipelineCase needCase = (ImagePipelineCase) contextProvider.get(step.getCaseClazz());
            executor.execute(
                    needCase,
                    context,
                    step.getParams()
            );

            long end = System.currentTimeMillis() - start;

            costsList.add(new PipelineOutput.OperationCost(NameUtils.name(step.getCaseClazz()), end));
        }

        return new PipelineOutput(context, costsList);
    }


}
