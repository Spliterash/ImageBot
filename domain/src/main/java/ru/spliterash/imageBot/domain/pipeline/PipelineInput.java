package ru.spliterash.imageBot.domain.pipeline;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.entities.Data;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PipelineInput {
    private final List<PipelineStep<?, ?>> steps;
    private final List<Data> dataLoaders;
}
