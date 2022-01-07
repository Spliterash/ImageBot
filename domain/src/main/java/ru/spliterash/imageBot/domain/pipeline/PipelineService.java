package ru.spliterash.imageBot.domain.pipeline;

import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.def.CaseExecutor;
import ru.spliterash.imageBot.domain.def.CaseIO;
import ru.spliterash.imageBot.domain.def.bean.Bean;

import java.util.List;

@RequiredArgsConstructor
public class PipelineService implements Bean {
    private final CaseExecutor executor;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public CaseIO run(List<PipelineStep<?, ?>> steps, CaseIO input) {
        return _run((List<PipelineStep>) (List<?>) steps, input);
    }

    @SuppressWarnings("rawtypes")
    private CaseIO _run(List<PipelineStep> steps, CaseIO input) {
        CaseIO data = input;

        for (PipelineStep step : steps) {
            //noinspection unchecked
            data = executor.execute(
                    step.getExecutedCase(),
                    data,
                    step.getParams()
            );
        }

        return data;
    }
}
