package ru.spliterash.imageBot.pipelines.text.types;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import ru.spliterash.imageBot.domain.cases.SliceDataCase;
import ru.spliterash.imageBot.domain.entities.Data;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.pipelines.text.TextTypes;
import ru.spliterash.imageBot.pipelines.text.def.AbstractCaseTextParser;
import ru.spliterash.imageBot.pipelines.text.exception.CommandParseException;

import java.util.Arrays;
import java.util.List;

/**
 * Синтаксис команды
 * удалить :-1 - Удалить последюю картинку с конца
 * удалить :2 - удалить первые 2 картинки
 * удалить 2 - удалить картинку под номером 2
 */
public class SliceParser extends AbstractCaseTextParser<SliceDataCase, SliceDataCase.Input> {

    public SliceParser(SliceDataCase realCase) {
        super(realCase);
    }

    @Override
    public List<String> getCmds() {
        return Arrays.asList(
                "slice",
                "удалить"
        );
    }

    @Override
    public Options getOptions() {
        return new Options()
                .addOption(Option.builder()
                        .option("t")
                        .longOpt("type")
                        .desc("С каким типом данных выполняется операция (image|text). По дефолту картинки")
                        .hasArg()
                        .required(false)
                        .build()
                );
    }

    @Override
    protected SliceDataCase.Input parseParams(CommandLine line) {
        String finalStr = String.join("", line.getArgList()).replaceAll(" +", "");

        SliceDataCase.Input.Type type;
        if (finalStr.startsWith(":")) {
            type = SliceDataCase.Input.Type.SLICE;
            finalStr = finalStr.substring(1);
        } else
            type = SliceDataCase.Input.Type.INDEX;
        int index;
        try {
            index = Integer.parseInt(finalStr);
        } catch (Exception exception) {
            throw new CommandParseException("введите число");
        }

        String typeRaw = line.getOptionValue("т", TextTypes.IMAGE);

        Class<? extends Data> dataClass = TextTypes.TYPE_MAP.getOrDefault(typeRaw, ImageData.class);

        return SliceDataCase.Input.builder()
                .operation(index)
                .type(type)
                .dataType(dataClass)
                .build();
    }
}
