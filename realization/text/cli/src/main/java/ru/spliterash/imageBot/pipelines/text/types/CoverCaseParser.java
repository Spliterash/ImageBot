package ru.spliterash.imageBot.pipelines.text.types;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import ru.spliterash.imageBot.domain.cases.CoverImageUseCase;
import ru.spliterash.imageBot.pipelines.text.def.AbstractCLICaseParser;
import ru.spliterash.imageBot.pipelines.text.utils.ParseUtils;

import java.util.Arrays;
import java.util.List;

public class CoverCaseParser extends AbstractCLICaseParser<CoverImageUseCase, CoverImageUseCase.Input> {
    private final ParseUtils parseUtils;

    public CoverCaseParser(CoverImageUseCase realCase, ParseUtils parseUtils) {
        super(realCase);
        this.parseUtils = parseUtils;
    }

    @Override
    public List<String> getCmds() {
        return Arrays.asList(
                "resize",
                "ресайз",
                "ресайc",
                "размер"
        );
    }

    @Override
    public Options getOptions() {
        return new Options()
                .addOption(
                        Option.builder()
                                .option("w")
                                .longOpt("width")
                                .hasArg()
                                .build())
                .addOption(
                        Option.builder()
                                .option("h")
                                .longOpt("height")
                                .hasArg()
                                .build()
                )
                .addOption(
                        Option.builder()
                                .option("c")
                                .longOpt("cut")
                                .hasArg()
                                .required(false)
                                .desc("Вырезать ли у изображения его части, чтобы оно идеально вошло в новый размер, или же оставлять пустое место")
                                .build()
                );
    }

    @Override
    protected CoverImageUseCase.Input parseParams(CommandLine line) {
        CoverImageUseCase.Input.InputBuilder<?, ?> builder = CoverImageUseCase.Input.builder();

        parseUtils.setIfPresent(line.getOptionValue("w"), w -> builder.width(parseUtils.parseInt(w)));
        parseUtils.setIfPresent(line.getOptionValue("h"), h -> builder.height(parseUtils.parseInt(h)));
        parseUtils.setIfPresent(line.getOptionValue("c"), c -> builder.cutImage(parseUtils.parseBoolean(c)));

        return builder.build();
    }
}
