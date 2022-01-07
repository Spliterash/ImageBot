package ru.spliterash.imageBot.pipelines.text.def;

import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.*;
import ru.spliterash.imageBot.domain.def.PipelineCase;
import ru.spliterash.imageBot.domain.def.annotation.NameUtils;
import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.pipeline.PipelineStep;
import ru.spliterash.imageBot.pipelines.text.exception.CommandParseException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractCaseTextParser<C extends PipelineCase<P>, P extends CaseParams> implements CaseTextParser<C, P> {
    private final C realCase;
    protected static final HelpFormatter formatter;

    static {
        formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("▶ ");
    }


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

    @Override
    public String help() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        String header = NameUtils.info(realCase.getClass());

        formatter.printHelp(writer, Integer.MAX_VALUE, String.join(",", getCmds()), header, getOptions(), 0, 0, footer());

        return stringWriter.toString();
    }

    protected String footer() {
        return null;
    }

    @Override
    public C getCase() {
        return realCase;
    }
}
