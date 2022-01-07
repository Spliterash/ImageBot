package ru.spliterash.imageBot.pipelines.text;

import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.def.bean.Bean;
import ru.spliterash.imageBot.domain.pipeline.PipelineStep;
import ru.spliterash.imageBot.pipelines.text.def.CaseTextParser;
import ru.spliterash.imageBot.pipelines.text.exception.CommandNotFound;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TextPipelineGenerator implements Bean {
    private final List<CaseTextParser<?, ?>> parsers;


    public List<PipelineStep<?, ?>> parse(String text) {
        String[] lines = text.split("\n");

        List<PipelineStep<?, ?>> steps = new ArrayList<>(lines.length);
        for (String line : lines) {
            line = line.trim();

            String firstWord = firstWord(line);

            CaseTextParser<?, ?> commandParser = parsers
                    .stream()
                    .filter(p -> p.getCmds().contains(firstWord.toLowerCase()))
                    .findFirst()
                    .orElseThrow(() -> new CommandNotFound("Команда " + firstWord + " не найдена"));
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

    private String firstWord(String line) {
        int spaceIndex = line.indexOf(" ");

        return spaceIndex > -1 ? line.substring(0, spaceIndex) : line;
    }
}
