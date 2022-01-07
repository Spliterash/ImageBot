package ru.spliterash.imageBot.pipelines.text.types;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import ru.spliterash.imageBot.domain.cases.GlueImagesCase;
import ru.spliterash.imageBot.pipelines.text.def.AbstractCaseTextParser;
import ru.spliterash.imageBot.pipelines.text.utils.ParseUtils;

import java.util.Arrays;
import java.util.List;

public class GlueParse extends AbstractCaseTextParser<GlueImagesCase, GlueImagesCase.GlueImagesParams> {
    private final ParseUtils parseUtils;

    public GlueParse(GlueImagesCase realCase, ParseUtils parseUtils) {
        super(realCase);
        this.parseUtils = parseUtils;
    }

    @Override
    public List<String> getCmds() {
        return Arrays.asList(
                "glue",
                "клей",
                "склеить"
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
                        .build())
                .addOption(Option.builder()
                        .option("p")
                        .longOpt("padding")
                        .desc("Отступ в пикселях в рамке изображения")
                        .required(false)
                        .hasArg()
                        .build())
                .addOption(Option.builder()
                        .option("c")
                        .longOpt("columns")
                        .desc("Количество столбцов")
                        .hasArg()
                        .build()
                )
                .addOption(Option.builder()
                        .option("r")
                        .longOpt("rows")
                        .desc("Количество строк")
                        .hasArg()
                        .build()
                )
                .addOption(Option.builder()
                        .required(false)
                        .option("m")
                        .longOpt("mode")
                        .hasArg()
                        .desc("Режим работы (cover|none). По дефолту cover")
                        .build())
                .addOption(Option.builder()
                        .required(false)
                        .option("ms")
                        .longOpt("max-size")
                        .hasArg()
                        .desc("Максимальный размер изображения по одной стороне в пикселях")
                        .build()
                );
    }

    @Override
    protected GlueImagesCase.GlueImagesParams parseParams(CommandLine line) {
        GlueImagesCase.GlueImagesParams.GlueImagesParamsBuilder<?, ?> builder = GlueImagesCase.GlueImagesParams.builder();

        parseUtils.setIfPresent(line.getOptionValue("b"), s -> builder.needBorder(parseUtils.parseBoolean(s)));
        parseUtils.setIfPresent(line.getOptionValue("p"), s -> builder.padding(parseUtils.parseInt(s)));
        parseUtils.setIfPresent(line.getOptionValue("c"), s -> builder.columns(parseUtils.parseInt(s)));
        parseUtils.setIfPresent(line.getOptionValue("r"), s -> builder.rows(parseUtils.parseInt(s)));
        parseUtils.setIfPresent(line.getOptionValue("m"), s -> builder.resizeMode(parseUtils.parseEnum(GlueImagesCase.ResizeMode.class, s)));
        parseUtils.setIfPresent(line.getOptionValue("ms"), s -> builder.max(parseUtils.parseInt(s)));

        return builder.build();
    }
}
