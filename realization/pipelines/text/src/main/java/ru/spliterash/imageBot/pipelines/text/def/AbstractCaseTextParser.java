package ru.spliterash.imageBot.pipelines.text.def;

import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.cli.*;
import ru.spliterash.imageBot.domain.def.PipelineCase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.pipeline.PipelineStep;
import ru.spliterash.imageBot.pipelines.text.exception.CommandParseException;

import java.util.List;

@SuperBuilder
@RequiredArgsConstructor
public abstract class AbstractCaseTextParser<C extends PipelineCase<P>, P extends CaseParams> implements CaseTextParser<C, P> {
    private final C realCase;


    public final PipelineStep<C, P> parse(String[] args) {
        P params;
        try {
            CommandLineParser parser = new DefaultParser();
            params = parseParams(parser.parse(getOptions(), args));
            return new PipelineStep<>(realCase, params);
        } catch (ParseException e) {
            throw new CommandParseException(e.getMessage(), e);
        }
    }

    public abstract List<String> getCmds();

    protected abstract Options getOptions();

    protected abstract P parseParams(CommandLine line);
}
