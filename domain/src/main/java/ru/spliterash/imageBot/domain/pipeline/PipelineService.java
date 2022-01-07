package ru.spliterash.imageBot.domain.pipeline;

import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.def.CaseExecutor;
import ru.spliterash.imageBot.domain.def.CaseIO;
import ru.spliterash.imageBot.domain.def.bean.Bean;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PipelineService implements Bean {
    private final CaseExecutor executor;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public PipelineOutput run(List<PipelineStep<?, ?>> steps, CaseIO input) {
        return _run((List<PipelineStep>) (List<?>) steps, input);
    }

    @SuppressWarnings("rawtypes")
    private PipelineOutput _run(List<PipelineStep> steps, CaseIO input) {
        CaseIO data = input;
        List<PipelineOutput.OperationCost> costsList = new ArrayList<>(steps.size());
        for (PipelineStep step : steps) {
            long start = System.currentTimeMillis();
            //noinspection unchecked
            data = executor.execute(
                    step.getExecutedCase(),
                    data,
                    step.getParams()
            );

            long end = System.currentTimeMillis() - start;

            costsList.add(new PipelineOutput.OperationCost(step.getExecutedCase().getClass().getSimpleName(), end));
        }

        return new PipelineOutput(data, costsList);
    }


}
