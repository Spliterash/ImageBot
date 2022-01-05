package ru.spliterash.imageBot.domain.pipeline;

import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.def.CaseExecutor;
import ru.spliterash.imageBot.domain.def.CaseIO;

import java.util.List;

@RequiredArgsConstructor
public class PipelineService {
    private final CaseExecutor executor;

    @SuppressWarnings("rawtypes")
    public CaseIO run(List<PipelineStep> steps, CaseIO input) {
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
