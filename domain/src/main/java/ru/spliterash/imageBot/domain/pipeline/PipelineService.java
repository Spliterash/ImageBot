package ru.spliterash.imageBot.domain.pipeline;

import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.def.CaseExecutor;

@RequiredArgsConstructor
public class PipelineService {
    private final CaseExecutor executor;
}
