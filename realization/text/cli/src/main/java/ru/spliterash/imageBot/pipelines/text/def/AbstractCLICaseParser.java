package ru.spliterash.imageBot.pipelines.text.def;

import net.jodah.typetools.TypeResolver;
import org.apache.commons.cli.*;
import ru.spliterash.imageBot.domain.def.ImagePipelineCase;
import ru.spliterash.imageBot.domain.def.annotation.NameUtils;
import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.pipeline.PipelineStep;
import ru.spliterash.imageBot.pipelines.text.exception.CommandParseException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;


public abstract class AbstractCLICaseParser<C extends ImagePipelineCase<P>, P extends CaseParams> implements CLICaseParser<C, P> {
    protected static final HelpFormatter formatter;
    protected final Class<C> caseClass;

    public AbstractCLICaseParser() {
        //noinspection unchecked
        this.caseClass = (Class<C>) TypeResolver.resolveRawArguments(AbstractCLICaseParser.class, getClass())[0];
    }

    static {
        formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("▶ ");
    }


    public final PipelineStep<C, P> parse(String[] args) {
        P params;
        try {
            CommandLineParser parser = new DefaultParser();
            params = parseParams(parser.parse(getOptions(), args));

            return new PipelineStep<>(params, getCase());
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

        String header = NameUtils.info(caseClass);

        formatter.printHelp(writer, Integer.MAX_VALUE, String.join(",", getCmds()), header, getOptions(), 0, 0, footer());

        return stringWriter.toString();
    }

    protected String footer() {
        return null;
    }

    @Override
    public Class<C> getCase() {
        return caseClass;
    }
}
