package ru.spliterash.imageBot.pipelines.text.types;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import ru.spliterash.imageBot.domain.cases.ESRGANCase;
import ru.spliterash.imageBot.pipelines.text.def.AbstractCaseTextParser;
import ru.spliterash.imageBot.pipelines.text.exception.CommandParseException;
import ru.spliterash.imageBot.pipelines.text.utils.ParseUtils;

import java.util.Arrays;
import java.util.List;

public class ESRGANParser extends AbstractCaseTextParser<ESRGANCase, ESRGANCase.Params> {
    private final ParseUtils parseUtils;

    public ESRGANParser(ESRGANCase realCase, ParseUtils parseUtils) {
        super(realCase);
        this.parseUtils = parseUtils;
    }

    @Override
    public List<String> getCmds() {
        return Arrays.asList(
                "esr",
                "esrgan",
                "есрган"
        );
    }

    @Override
    protected Options getOptions() {
        return new Options();
    }

    @Override
    protected ESRGANCase.Params parseParams(CommandLine line) {
        List<String> argList = line.getArgList();
        if (argList.isEmpty())
            throw new CommandParseException("Пустая модель");

        return ESRGANCase.Params.builder()
                .model(parseUtils.parseInt(argList.get(0)) - 1)
                .build();
    }

    @Override
    protected String footer() {
        return String.join("\n", getCase().models());
    }
}
