package ru.spliterash.imageBot.domain.pipeline;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.def.ICase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;

@Getter
@RequiredArgsConstructor
public class PipelineStep<C extends ICase<P>, P extends CaseParams> {
    private final C executedCase;
    private final P params;
}