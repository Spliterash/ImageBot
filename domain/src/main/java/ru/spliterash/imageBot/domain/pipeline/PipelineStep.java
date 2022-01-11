package ru.spliterash.imageBot.domain.pipeline;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.def.ImagePipelineCase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;

@Getter
@RequiredArgsConstructor
public class PipelineStep<C extends ImagePipelineCase<P>, P extends CaseParams> {
    private final P params;
    private final Class<C> caseClazz;
}
