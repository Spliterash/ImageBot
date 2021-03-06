package ru.spliterash.imageBot.domain.pipeline;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.def.ImageCaseContext;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PipelineOutput {
    private final ImageCaseContext output;
    private final List<OperationCost> cost;

    @Getter
    @RequiredArgsConstructor
    public static class OperationCost {
        private final String operationName;
        private final long cost;
    }
}
