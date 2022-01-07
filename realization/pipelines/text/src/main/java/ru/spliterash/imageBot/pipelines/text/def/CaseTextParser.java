package ru.spliterash.imageBot.pipelines.text.def;

import ru.spliterash.imageBot.domain.def.PipelineCase;
import ru.spliterash.imageBot.domain.def.bean.Bean;
import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.pipeline.PipelineStep;

import java.util.List;

public interface CaseTextParser<C extends PipelineCase<P>, P extends CaseParams> extends Bean {
    List<String> getCmds();

    PipelineStep<C, P> parse(String[] args);

    String help();

    C getCase();
}
