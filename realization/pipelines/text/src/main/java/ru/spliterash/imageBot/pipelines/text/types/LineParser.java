package ru.spliterash.imageBot.pipelines.text.types;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import ru.spliterash.imageBot.domain.cases.LineImagesCase;
import ru.spliterash.imageBot.domain.entities.Direction;
import ru.spliterash.imageBot.pipelines.text.def.AbstractCaseTextParser;
import ru.spliterash.imageBot.pipelines.text.utils.ParseUtils;

import java.util.Arrays;
import java.util.List;

public class LineParser extends AbstractCaseTextParser<LineImagesCase, LineImagesCase.Params> {
    private final ParseUtils parseUtils;

    public LineParser(LineImagesCase realCase, ParseUtils parseUtils) {
        super(realCase);
        this.parseUtils = parseUtils;
    }

    @Override
    public List<String> getCmds() {
        return Arrays.asList(
                "лайн",
                "линия",
                "line"
        );
    }

    @Override
    protected Options getOptions() {
        return new Options()
                .addOption(Option.builder()
                        .option("b")
                        .longOpt("border")
                        .desc("Нужно ли делать линии между изображениями")
                        .required(false)
                        .hasArg()
                        .build());
    }

    @Override
    protected LineImagesCase.Params parseParams(CommandLine line) {
        LineImagesCase.Params.ParamsBuilder<?, ?> builder = LineImagesCase.Params.builder();

        parseUtils.setIfPresent(line.getOptionValue("b"), s -> builder.needBorder(parseUtils.parseBoolean(s)));

        if (line.getArgList().size() > 0)
            builder.direction(parseUtils.parseEnum(Direction.class, line.getArgList().get(0)));

        return builder.build();
    }

    @Override
    protected String footer() {
        return "Режим работы (vertical|horizontal). По дефолту horizontal";
    }
}
