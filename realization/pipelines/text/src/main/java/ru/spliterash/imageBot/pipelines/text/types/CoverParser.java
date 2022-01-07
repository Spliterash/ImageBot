package ru.spliterash.imageBot.pipelines.text.types;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import ru.spliterash.imageBot.domain.cases.CoverImageUseCase;
import ru.spliterash.imageBot.pipelines.text.def.AbstractCaseTextParser;
import ru.spliterash.imageBot.pipelines.text.utils.ParseUtils;

import java.util.Arrays;
import java.util.List;

public class CoverParser extends AbstractCaseTextParser<CoverImageUseCase, CoverImageUseCase.Input> {
    private final ParseUtils parseUtils;

    public CoverParser(CoverImageUseCase realCase, ParseUtils parseUtils) {
        super(realCase);
        this.parseUtils = parseUtils;
    }

    @Override
    public List<String> getCmds() {
        return Arrays.asList(
                "cover",
                "обложка"
        );
    }

    @Override
    public Options getOptions() {
        return new Options()
                .addOption(
                        Option.builder()
                                .option("w")
                                .longOpt("width")
                                .required(true)
                                .hasArg()
                                .build())
                .addOption(
                        Option.builder()
                                .option("h")
                                .longOpt("height")
                                .required(true)
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
        int width = parseUtils.parseInt(line.getOptionValue("w"));
        int height = parseUtils.parseInt(line.getOptionValue("h"));
        boolean cut = parseUtils.parseBoolean(line.getOptionValue("c", "true"));

        return CoverImageUseCase.Input.builder()
                .width(width)
                .height(height)
                .cutImage(cut)
                .build();
    }
}
