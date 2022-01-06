package ru.spliterash.imageBot.pipelines.text.def;

import ru.spliterash.imageBot.domain.def.Bean;
import ru.spliterash.imageBot.domain.def.ICase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.pipeline.PipelineStep;

import java.util.List;

public interface CaseTextParser<C extends ICase<P>, P extends CaseParams> extends Bean {
    List<String> getCmds();

    PipelineStep<C, P> parse(String[] args);
}
