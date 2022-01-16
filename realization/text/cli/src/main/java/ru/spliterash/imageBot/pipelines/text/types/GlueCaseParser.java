package ru.spliterash.imageBot.pipelines.text.types;

import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import ru.spliterash.imageBot.domain.cases.GlueImageCase;
import ru.spliterash.imageBot.pipelines.text.def.AbstractCLICaseParser;
import ru.spliterash.imageBot.pipelines.text.utils.ParseUtils;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class GlueCaseParser extends AbstractCLICaseParser<GlueImageCase, GlueImageCase.Params> {
    private final ParseUtils parseUtils;

    @Override
    public List<String> getCmds() {
        return Arrays.asList(
                "glue",
                "клей",
                "склей",
                "склеить",
                "слепи"
        );
    }

    @Override
    public Options getOptions() {
        return new Options()
                .addOption(Option.builder()
                        .option("b")
                        .longOpt("border")
                        .desc("Нужно ли делать линии между изображениями")
                        .required(false)
                        .hasArg()
                        .build()
                );
    }

    @Override
    protected GlueImageCase.Params parseParams(CommandLine line) {
        GlueImageCase.Params.ParamsBuilder<?, ?> builder = GlueImageCase.Params.builder();
        parseUtils.setIfPresent(line.getOptionValue("b"), s -> builder.needBorder(parseUtils.parseBoolean(s)));
        return builder.build();
    }
}
