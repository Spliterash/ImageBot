package ru.spliterash.imageBot.pipelines.text.types;

import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import ru.spliterash.imageBot.domain.cases.CropImageUseCase;
import ru.spliterash.imageBot.pipelines.text.def.AbstractCLICaseParser;
import ru.spliterash.imageBot.pipelines.text.utils.ParseUtils;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class CropCaseParser extends AbstractCLICaseParser<CropImageUseCase, CropImageUseCase.Params> {
    private final ParseUtils pu;

    @Override
    public List<String> getCmds() {
        return Arrays.asList(
                "crop",
                "кроп",
                "обрезка",
                "обрезать"
        );
    }

    @Override
    protected Options getOptions() {
        return new Options()
                .addOption(Option.builder()
                        .option("a")
                        .longOpt("all")
                        .hasArg()
                        .desc("Размер кропа со всех сторон")
                        .build())
                .addOption(Option.builder()
                        .option("l")
                        .longOpt("left")
                        .hasArg()
                        .desc("Размер кропа с левой стороны")
                        .build())
                .addOption(Option.builder()
                        .option("t")
                        .longOpt("top")
                        .hasArg()
                        .desc("Размер кропа сверху")
                        .build())
                .addOption(Option.builder()
                        .option("b")
                        .longOpt("bottom")
                        .hasArg()
                        .desc("Размер кропа снизу")
                        .build())
                .addOption(Option.builder()
                        .option("r")
                        .longOpt("right")
                        .hasArg()
                        .desc("Размер кропа слева")
                        .build()
                );
    }

    @Override
    protected CropImageUseCase.Params parseParams(CommandLine line) {
        CropImageUseCase.Params.ParamsBuilder<?, ?> builder = CropImageUseCase.Params.builder();

        pu.setIfPresent(line.getOptionValue("a"), s -> builder.all(pu.parseInt(s)));
        pu.setIfPresent(line.getOptionValue("l"), s -> builder.left(pu.parseInt(s)));
        pu.setIfPresent(line.getOptionValue("t"), s -> builder.top(pu.parseInt(s)));
        pu.setIfPresent(line.getOptionValue("b"), s -> builder.bottom(pu.parseInt(s)));
        pu.setIfPresent(line.getOptionValue("r"), s -> builder.right(pu.parseInt(s)));

        return builder.build();
    }
}
