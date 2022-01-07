package ru.spliterash.imageBot.pipelines.text;

import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.def.PipelineCase;
import ru.spliterash.imageBot.domain.def.bean.Bean;
import ru.spliterash.imageBot.domain.pipeline.PipelineStep;
import ru.spliterash.imageBot.domain.utils.MyStringUtils;
import ru.spliterash.imageBot.pipelines.text.def.CaseTextParser;
import ru.spliterash.imageBot.pipelines.text.exception.CaseParserNotFound;
import ru.spliterash.imageBot.pipelines.text.exception.PipelineCommandNotFound;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TextPipelineGenerator implements Bean {
    private final List<CaseTextParser<?, ?>> parsers;


    public <T extends PipelineCase<?>> CaseTextParser<T, ?> findCase(Class<T> clazz) {
        //noinspection unchecked
        return (CaseTextParser<T, ?>) parsers
                .stream()
                .filter(p -> clazz.isAssignableFrom(p.getCase().getClass()))
                .findFirst()
                .orElseThrow(() -> new CaseParserNotFound(clazz));
    }

    public List<PipelineStep<?, ?>> parse(String[] lines) {
        List<PipelineStep<?, ?>> steps = new ArrayList<>(lines.length);
        for (String line : lines) {
            line = line.trim();

            String firstWord = MyStringUtils.firstWord(line);

            CaseTextParser<?, ?> commandParser = parsers
                    .stream()
                    .filter(p -> p.getCmds().contains(firstWord.toLowerCase()))
                    .findFirst()
                    .orElseThrow(() -> new PipelineCommandNotFound(firstWord));

            String[] args;

            if (firstWord.equals(line))
                args = new String[0];
            else
                args = line.substring(firstWord.length() + 1).split(" +");

            PipelineStep<?, ?> parsed = commandParser.parse(args);
            steps.add(parsed);
        }

        return steps;
    }
}
